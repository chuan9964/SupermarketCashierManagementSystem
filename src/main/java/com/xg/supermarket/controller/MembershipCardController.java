package com.xg.supermarket.controller;

import com.github.pagehelper.PageInfo;
import com.xg.supermarket.pojo.MembershipCard;
import com.xg.supermarket.service.MembershipCardService;
import com.xg.supermarket.service.OrderService;
import com.xg.supermarket.utils.ReMap;
import com.xg.supermarket.utils.ReMapUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MembershipCardController {

    @Autowired
    private MembershipCardService membershipCardService;

    @RequiresPermissions("membershipCard:view")
    @RequestMapping("membershipCard")
    public String membershipCard(){
        return "membershipCard";
    }

    /**
     * 分页查询会员卡信息（也可条件查询）
     * @param pageNum
     * @param pageSize
     * @param name
     * @param mno
     * @param phone
     * @param startTime
     * @param endTime
     * @return
     */
    @RequiresPermissions("membershipCard:page")
    @PostMapping("membershipCard/pageMembershipCard")
    @ResponseBody
    public Map pageMembershipCard(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                  @RequestParam(name = "pageSize",defaultValue = "0")Integer pageSize,
                                  String name, String mno, String phone, String startTime, String endTime){
        PageInfo<MembershipCard> cardPageInfo = membershipCardService.pageMembershipCard(pageNum, pageSize,name,mno,phone,startTime,endTime);
        Map map = new HashMap<>();
        map.put("total",cardPageInfo.getTotal());
        map.put("rows",cardPageInfo.getList());
        return map;
    }

    @RequiresPermissions("membershipCard:add")
    @PostMapping("membershipCard/addMembershipCard")
    @ResponseBody
    public ReMap addMembershipCard(MembershipCard membershipCard){
        membershipCardService.addMembershipCard(membershipCard);
        return ReMapUtil.success("办理会员卡成功").setStatus("success");
    }
    @RequiresPermissions("membershipCard:update")
    @PostMapping("membershipCard/updateMembershipCard")
    @ResponseBody
    public ReMap updateMembershipCard(MembershipCard membershipCard){
        membershipCardService.updateMembershipCard(membershipCard);
        return ReMapUtil.success("修改会员卡成功").setStatus("success");
    }
    @RequiresPermissions("membershipCard:del")
    @PostMapping("membershipCard/delMembershipCard")
    @ResponseBody
    public ReMap delMembershipCard(Integer mid){
        membershipCardService.delMembershipCard(mid);
        return ReMapUtil.success("删除会员卡成功").setStatus("success");
    }

    /**
     * 使用会员卡刷卡支付，更新会员卡余额
     * @param mno
     * @param price
     * @return
     */
    @PutMapping("/membershipCard/updateBalance")
    @ResponseBody
    public ReMap updateBalance(String mno,BigDecimal price,Integer oid){
        membershipCardService.updateBalance(mno,price,oid);
        return ReMapUtil.success("支付成功").setStatus("success");
    }
}
