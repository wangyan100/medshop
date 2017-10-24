/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.model;

import java.math.BigDecimal;

/**
 *
 * @author yw
 */
public class StatisticReport {
    private String shopname;
    private String tourguidename;

    public String getTourguidename() {
        return tourguidename;
    }

    public void setTourguidename(String tourguidename) {
        this.tourguidename = tourguidename;
    }
    
    private BigDecimal totalprice;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }
    

   
}
