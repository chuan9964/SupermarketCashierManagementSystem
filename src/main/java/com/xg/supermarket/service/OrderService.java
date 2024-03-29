package com.xg.supermarket.service;

import com.github.pagehelper.PageInfo;
import com.xg.supermarket.pojo.Order;
import com.xg.supermarket.pojo.OrderGoods;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chuan9964
 * @version 1.0.0
 * @ClassName OrderService.java
 * @Description 订单服务层接口
 * @createTime 2023年06月25日 14:51:00
 */
public interface OrderService {
    /**
     * @title createOrder
     * @description 创建订单根据挂单编号创建订单返回订单ID
     * @author chuan9964
     * @updateTime 2021/6/25 14:52
     * @throws
     */
    Integer createOrder(String no,String ono,String operator);
    /**
     * @title payOrder
     * @description 订单支付
     * @author chuan9964
     * @updateTime 2021/6/26 15:01
     * @throws
     */
    Integer payOrder(Integer oid, List<Integer> typeids, List<BigDecimal> prices);
    /**
     * @title pageOrder
     * @description 分页查询订单数据
     * @author chuan9964
     * @updateTime 2021/6/28 8:57
     * @throws
     */
    PageInfo<Order> pageOrder(Integer pageNum,Integer pageSize, String ono, Double minPrice, Double maxPrice, String startTime, String endTime, Integer status, String operator);

    /**
     * 根据订单编号查询订单详情信息
     * @param oid
     * @return
     */
    List<OrderGoods> selectOrderByOId(Integer oid);

    /**
     *  根据订单编号生成支付码
     * @param oid
     * @return
     */
    String getOrderPeyCode(String oid);

    /**
     * 根据订单编号修改订单状态
     * @param oid
     * @return
     */
    int updateOrderIsPay(Integer oid);

    /**
     * 根据订单id查询订单表
     * @param oid
     * @return
     */
    Order selectOrder(Integer oid);
}
