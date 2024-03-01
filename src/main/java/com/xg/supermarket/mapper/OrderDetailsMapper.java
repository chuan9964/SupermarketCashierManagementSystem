package com.xg.supermarket.mapper;

import com.xg.supermarket.pojo.OrderDetails;
import com.xg.supermarket.pojo.OrderGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author chuan9964
 * @version 1.0.0
 * @ClassName OrderDetailsMapper.java
 * @Description 订单详情Dao
 * @createTime 2023年06月25日 15:16:00
 */
@Repository
public interface OrderDetailsMapper extends Mapper<OrderDetails>, InsertListMapper<OrderDetails> {
    @Select("SELECT od.`name`,od.price,od.number,g.`code`,g.img FROM order_details od,goods g WHERE od.gid = g.gid AND od.oid = #{ono}")
    List<OrderGoods> selectOrderByOId(@Param("ono") Integer ono);
}
