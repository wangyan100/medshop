/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.model;

/**
 *
 * @author yw
 */
public class ExcelLine {
    //here is wrapper of Exceline
    // 
    
    private String pzn;
    private String germanName;
    private String chineseName;
    private String unit;
    private String price;
   // private String currency;
    private int rowCount;

    public String getPzn() {
        return pzn;
    }

    public void setPzn(String pzn) {
        this.pzn = pzn;
    }

    public String getGermanName() {
        return germanName;
    }

    public void setGermanName(String germanName) {
        this.germanName = germanName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    
}
