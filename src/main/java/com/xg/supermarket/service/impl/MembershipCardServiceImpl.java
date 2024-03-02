package com.xg.supermarket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xg.supermarket.exception.BizException;
import com.xg.supermarket.mapper.MembershipCardMapper;
import com.xg.supermarket.pojo.MembershipCard;
import com.xg.supermarket.service.MembershipCardService;
import com.xg.supermarket.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
@Transactional
public class MembershipCardServiceImpl implements MembershipCardService {

    @Autowired
    private MembershipCardMapper membershipCardMapper;


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
        return membershipCardMapper.updateBalance(membershipCard);
    }

    @Override
    public int delMembershipCard(Integer mid) {
        return membershipCardMapper.deleteByPrimaryKey(mid);
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
