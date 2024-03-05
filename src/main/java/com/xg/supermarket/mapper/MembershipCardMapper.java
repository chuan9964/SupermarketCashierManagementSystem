package com.xg.supermarket.mapper;

import com.xg.supermarket.pojo.MembershipCard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface MembershipCardMapper extends Mapper<MembershipCard> {

    /**
     * 更新余额
     * @param mno
     * @param price
     * @return
     */
    @Update("update membership_card set balance = balance - #{price} where mno = #{mno}")
    int updateBalance(@Param("mno") String mno, @Param("price") BigDecimal price);

    /**
     * 根据会员卡号查找会员信息
     * @param mno
     * @return
     */
    @Select("select * from membership_card where mno=#{mno}")
    MembershipCard selectCardByMno(String mno);
}
