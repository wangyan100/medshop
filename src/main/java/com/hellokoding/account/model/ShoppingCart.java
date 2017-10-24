/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.model;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author yw
 */
public class ShoppingCart {
    
    private java.util.Map<Product,Integer> orders;
    private String shopName;
    private String tourGuideName;
    private String tourGuideID;
    private String touristName;
    private String orderCreator;

    public String getOrderCreator() {
        return orderCreator;
    }

    public void setOrderCreator(String orderCreator) {
        this.orderCreator = orderCreator;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }
    private Date pickupDate;
    private String pickupTime;

    public Map<Product, Integer> getOrders() {
        return orders;
    }

    public void setOrders(Map<Product, Integer> orders) {
        this.orders = orders;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
    
    
    
}
