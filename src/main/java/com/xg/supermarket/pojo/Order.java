package com.xg.supermarket.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Table(name = "`order`")
public class Order {
    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;

    /**
     * 订单编号
     */
    private String ono;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日 HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 支付状态
     */
    private Integer isPay;

    public Order() {
    }

    public Order(String ono, BigDecimal price, Date createTime, String operator, Integer status) {
        this.ono = ono;
        this.price = price;
        this.createTime = createTime;
        this.operator = operator;
        this.status = status;
    }

    public Order(Integer oid, String ono, BigDecimal price, Date createTime, String operator, Integer status, Integer isPay) {
        this.oid = oid;
        this.ono = ono;
        this.price = price;
        this.createTime = createTime;
        this.operator = operator;
        this.status = status;
        this.isPay = isPay;
    }

    /**
     * 获取订单ID
     *
     * @return oid - 订单ID
     */
    public Integer getOid() {
        return oid;
    }

    /**
     * 设置订单ID
     *
     * @param oid 订单ID
     */
    public void setOid(Integer oid) {
        this.oid = oid;
    }

    /**
     * 获取订单编号
     *
     * @return ono - 订单编号
     */
    public String getOno() {
        return ono;
    }

    /**
     * 设置订单编号
     *
     * @param ono 订单编号
     */
    public void setOno(String ono) {
        this.ono = ono;
    }

    /**
     * 获取单价
     *
     * @return price - 单价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置单价
     *
     * @param price 单价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取操作人
     *
     * @return operator - 操作人
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作人
     *
     * @param operator 操作人
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取支付状态
     * @return 状态
     */
    public Integer getIsPay() {
        return isPay;
    }
    /**
     * 设置支付状态
     */
    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }
}