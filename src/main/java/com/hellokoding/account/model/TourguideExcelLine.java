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
public class TourguideExcelLine {
    
    private String tourguideid;
    private String name;
    private String chinesename;
    private int rowCount;

    public String getTourguideid() {
        return tourguideid;
    }

    public void setTourguideid(String tourguideid) {
        this.tourguideid = tourguideid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChinesename() {
        return chinesename;
    }

    public void setChinesename(String chinesename) {
        this.chinesename = chinesename;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    
}
