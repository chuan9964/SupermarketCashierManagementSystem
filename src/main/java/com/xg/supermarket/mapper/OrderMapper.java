package com.xg.supermarket.mapper;

import com.xg.supermarket.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author chuan9964
 * @version 1.0.0
 * @ClassName OrderMapper.java
 * @Description 订单Dao
 * @createTime 2023年06月25日 15:15:00
 */
@Repository
public interface OrderMapper extends Mapper<Order> {

    @Update("UPDATE `order` set is_pay = 2 where oid=#{oid}")
    int updateOrderIsPay(@Param("oid") Integer oid);
}
