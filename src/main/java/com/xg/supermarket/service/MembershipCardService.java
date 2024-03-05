package com.xg.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.xg.supermarket.pojo.MembershipCard;

import java.math.BigDecimal;

public interface MembershipCardService {

    PageInfo<MembershipCard> pageMembershipCard(Integer pageNum, Integer pageSize, String name, String mno, String phone, String startTime, String endTime);

    int addMembershipCard(MembershipCard membershipCard);

    int updateMembershipCard(MembershipCard membershipCard);

    int delMembershipCard(Integer mid);

    int updateBalance(String mno, BigDecimal price, Integer oid);
}
