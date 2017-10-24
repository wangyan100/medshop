/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.model;

import java.sql.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author yw
 */
@Entity
@Table(name = "productorder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderDetail> orderdetails;
    private String shopname;
    private java.sql.Date pickupDate;
    private String pickupTime;
    private String tourGuideName;
    private String tourGuideID;
    private String touristName;
    private java.sql.Date orderdate;
    private String orderNumber;
    private float totalPrice;
    private String creator;
    private String modifier;
    private String status;

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }
    


    /*
    `shopname` varchar(255) DEFAULT NULL,
  `tourguidename` varchar(255) DEFAULT NULL,
  `tourguideid` varchar(255) DEFAULT NULL,
  `touristname` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `orderdate`  date DEFAULT NULL,
  `ordernumber`  varchar(255) DEFAULT NULL,
  `totalprice` DECIMAL(7,2) DEFAULT NULL,
  `creater`    varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `status`   varchar(255) DEFAULT NULL,
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderDetail> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<OrderDetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public String getTourGuideID() {
        return tourGuideID;
    }

    public void setTourGuideID(String tourGuideID) {
        this.tourGuideID = tourGuideID;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreater(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
