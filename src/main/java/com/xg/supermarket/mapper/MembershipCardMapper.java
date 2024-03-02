package com.xg.supermarket.mapper;

import com.xg.supermarket.pojo.MembershipCard;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipCardMapper extends Mapper<MembershipCard> {

    /**
     * 充值余额
     * @param membershipCard
     * @return
     */
    @Update("update membership_card set balance = #{balance} where mid = #{mid}")
    int updateBalance(MembershipCard membershipCard);
}
