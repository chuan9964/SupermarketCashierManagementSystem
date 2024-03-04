package com.xg.supermarket.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.xg.supermarket.config.ConstantsConfig;
import com.xg.supermarket.pojo.Order;
import com.xg.supermarket.pojo.User;
import com.xg.supermarket.service.OrderService;
import com.xg.supermarket.utils.ReMap;
import com.xg.supermarket.utils.ReMapUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chuan9964
 * @version 1.0.0
 * @ClassName OrderController.java
 * @Description 订单控制器
 * @createTime 2023年06月25日 14:47:00
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * @title order
     * @description 返回订单管理视图
     * @author chuan9964
     * @updateTime 2023/6/28 8:51
     * @throws
     */
    @RequiresPermissions("order:view")
    @RequestMapping("order")
    public String order(){
        return "order";
    }

    /**
     * @title createOrder
     * @description 根据用户传递的挂单编号创建订单
     * @author chuan9964
     * @updateTime 2023/6/25 14:49
     * @throws
     */
    @RequiresPermissions("order:create")
    @PostMapping("/order/createOrder")
    @ResponseBody
    public ReMap createOrder(String ono, HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        return ReMapUtil.success(orderService.createOrder(request.getSession().getId(),ono,user.getRealName()));
    }

    /**
     *  根据订单编号生成支付码
     * @param oid
     * @param request
     * @return
     */
    @RequiresPermissions("order:getOrderPeyCode")
    @PostMapping("/order/getOrderPeyCode")
    @ResponseBody
    public ReMap getOrderPeyCode(String oid, HttpServletRequest request){
        // 获取协议（http或https）
        String scheme = request.getScheme();

        // 获取主机名和端口号
        String host = request.getHeader("Host");
        int port = request.getServerPort();

        // 构建完整的URL
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(scheme).append("://").append(host);

        // 如果端口不是默认的HTTP或HTTPS端口，则添加端口号
//        if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
//            urlBuilder.append(":").append(port);
//        }

        // 获取支付页面的url
        urlBuilder.append("/order/getWXPay?oid=").append(oid);

        String url = urlBuilder.toString();
        return ReMapUtil.success(orderService.getOrderPeyCode(url));
    }

    /**
     * 将订单信息传递到确认订单页面
     * @param oid
     * @return
     */
    @RequiresPermissions("order:getWXPay")
    @GetMapping("/order/getWXPay")
    @ResponseBody
    public ModelAndView getWXPay(@RequestParam("oid") Integer oid){
        Map map = new HashMap();
        map.put("order",orderService.selectOrder(oid));
        return new ModelAndView ("confirmPay",map);
    }
    /**
     * 根据订单编号查询订单和商品详情信息
     * @param oid
     * @return
     */
    @GetMapping("/order/selectOrderByOId")
    @ResponseBody
    public ReMap selectOrderByOId(@RequestParam("oid") Integer oid){
        return ReMapUtil.success(orderService.selectOrderByOId(oid));
    }

    /**
     * @title payOrder
     * @description 订单支付
     * @author chuan9964
     * @updateTime 2023/6/26 15:00
     * @throws
     */
    @RequiresPermissions("order:payOrder")
    @PostMapping("/order/payOrder")
    @ResponseBody
    public ReMap payOrder(Integer oid,String typeids,String prices,String cno,HttpServletRequest request){
        List<Integer> integers = JSONArray.parseArray(typeids, Integer.class);
        List<BigDecimal> bigDecimals = JSONArray.parseArray(prices, BigDecimal.class);
        orderService.payOrder(oid,integers,bigDecimals);
        ConstantsConfig.cashierMap.get(request.getSession().getId()).put(cno,new ArrayList<>());
        return ReMapUtil.success();
    }
    @RequiresPermissions("order:page")
    @PostMapping("order/pageOrder")
    @ResponseBody
    public Map pageOrder(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(name = "pageSize",defaultValue = "0") Integer pageSize, String ono, Double minPrice, Double maxPrice, String startTime, String endTime, Integer status, String operator){
        PageInfo<Order> pageInfo = orderService.pageOrder(pageNum, pageSize, ono, minPrice, maxPrice, startTime, endTime, status, operator);
        Map map = new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }

    /**
     * 根据订单编号查询订单列表
     * @param oid
     * @return
     */
    @GetMapping("/order/selectOrder")
    @ResponseBody
    public ReMap selectOrder(@RequestParam("oid") Integer oid){
        return ReMapUtil.success(orderService.selectOrder(oid));
    }

    /**
     * 根据订单编号进行修改订单状态
     * @param oid
     * @return
     */
    @RequiresPermissions("order:update")
    @GetMapping("order/updateOrder")
    @ResponseBody
    public ModelAndView updateOrder(@RequestParam("oid") Integer oid, @RequestParam("price") BigDecimal price) {
        orderService.updateOrder(oid);
        return new ModelAndView("paySuccess").addObject("price",price);
    }

}
