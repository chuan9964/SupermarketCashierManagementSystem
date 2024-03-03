package com.xg.supermarket.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class MembershipCard {

    /**
     * 会员卡id
     */
    @Id
    private Integer mid;

    /**
     * 会员卡号
     */
    private String mno;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 办卡时间
     */
    @Column(name = "applycard_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日 HH:mm:ss", timezone = "GMT+8")
    private Date applyCardTime;

    /**
     * 是否启用
     */
    @Column(name = "isenable")
    private Integer isEnable;

    public MembershipCard() {
    }

    public MembershipCard(Integer mid, String mno, BigDecimal balance, String name, String phone, Date applyCardTime, Integer isEnable) {
        this.mid = mid;
        this.mno = mno;
        this.balance = balance;
        this.name = name;
        this.phone = phone;
        this.applyCardTime = applyCardTime;
        this.isEnable = isEnable;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getApplyCardTime() {
        return applyCardTime;
    }

    public void setApplyCardTime(Date applyCardTime) {
        this.applyCardTime = applyCardTime;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
