package com.xg.supermarket.pojo;

import javax.persistence.Id;
import java.math.BigDecimal;

public class OrderGoods {

    /**
     * 订单详情ID
     */
    @Id
    private Integer odid;

    /**
     * 商品名
     */
    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer number;


    /**
     * 商品图片
     */
    private String img;

    /**
     * 商品条码
     */
    private String code;

    public OrderGoods() {
    }

    public OrderGoods(Integer odid, String name, BigDecimal price, Integer number, String img, String code) {
        this.odid = odid;
        this.name = name;
        this.price = price;
        this.number = number;
        this.img = img;
        this.code = code;
    }

    public Integer getOdid() {
        return odid;
    }

    public void setOdid(Integer odid) {
        this.odid = odid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
