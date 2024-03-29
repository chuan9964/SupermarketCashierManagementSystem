package com.xg.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xg.supermarket.exception.BizException;
import com.xg.supermarket.mapper.MembershipCardMapper;
import com.xg.supermarket.mapper.OrderMapper;
import com.xg.supermarket.pojo.MembershipCard;
import com.xg.supermarket.service.MembershipCardService;
import com.xg.supermarket.utils.DateUtil;
import com.xg.supermarket.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MembershipCardServiceImpl implements MembershipCardService {

    @Autowired
    private MembershipCardMapper membershipCardMapper;

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public PageInfo<MembershipCard> pageMembershipCard(Integer pageNum, Integer pageSize, String name, String mno, String phone, String startTime, String endTime) {

        Example example = new Example(MembershipCard.class);
        Example.Criteria criteria = example.createCriteria();

        if(StringUtil.isNotEmpty(name)){
            criteria.andLike("name","%"+name+"%");
        }
        if(StringUtil.isNotEmpty(mno)){
            criteria.andEqualTo("mno",mno);
        }
        if(StringUtil.isNotEmpty(phone)){
            criteria.andEqualTo("phone",phone);
        }
        if(StringUtil.isNotEmpty(startTime)){
            criteria.andGreaterThanOrEqualTo("applycard_time", DateUtil.getDate(startTime));
        }
        if(StringUtil.isNotEmpty(endTime)){
            criteria.andLessThanOrEqualTo("applycard_time", DateUtil.getDate(endTime));
        }
        PageHelper.startPage(pageNum,pageSize);
        List<MembershipCard> membershipCard = membershipCardMapper.selectByExample(example);
        return new PageInfo<>(membershipCard);
    }

    @Override
    public int addMembershipCard(MembershipCard membershipCard) {
        regMembershipCard(membershipCard,false);
        return membershipCardMapper.insertSelective(membershipCard);
    }

    @Override
    public int updateMembershipCard(MembershipCard membershipCard) {
        regMembershipCard(membershipCard,true);
        return membershipCardMapper.updateByPrimaryKeySelective(membershipCard);
    }

    @Override
    public int delMembershipCard(Integer mid) {
        return membershipCardMapper.deleteByPrimaryKey(mid);
    }

    /**
     * 使用会员卡刷卡支付，更新会员卡余额
     * @param mno
     * @param price
     * @param oid
     * @return
     */
    @Override
    public int updateBalance(String mno, BigDecimal price, Integer oid) {
        membershipCardMapper.updateBalance(mno,price);
        MembershipCard membershipCard = membershipCardMapper.selectCardByMno(mno);
        //发送短信告知消费者
        SmsUtil.SendSms(membershipCard.getPhone(),price,membershipCard.getBalance());
        //更新订单状态
        return orderMapper.updateOrderIsPay(oid);
    }

    private void regMembershipCard(MembershipCard membershipCard, boolean flag){
        if(membershipCard==null){
            throw new BizException("请检查会员卡数据");
        }
        if(flag && membershipCard.getMid()==null){
            throw new BizException("请检查会员卡参数，无会员卡ID");
        }

        if(StringUtil.isEmpty(membershipCard.getName())){
            throw new BizException("请检查会员卡参数，无持卡人");
        }

        if(StringUtil.isEmpty(membershipCard.getPhone())){
            throw new BizException("请检查会员卡参数，无手机号");
        }

        if(StringUtil.isEmpty(membershipCard.getMno())){
            throw new BizException("请检查会员卡参数，无会员卡号");
        }
    }
}
