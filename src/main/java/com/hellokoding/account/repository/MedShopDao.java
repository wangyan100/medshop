/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.repository;

import com.hellokoding.account.model.Order;
import com.hellokoding.account.model.OrderDetail;
import com.hellokoding.account.model.Product;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author yw
 */
public interface MedShopDao {

    void saveProduct(Product product);

    void updateProduct(Product product);

    void saveOrder(Order order);
    
    void saveOrderDetail(OrderDetail orderDetail);
    
    void deleteOrderDetail(long orderdetailid);
    
    void updateOrderDetail(OrderDetail orderDetail);

    Product getProductByPZN(String pzn);

    java.util.List<Product> getAllProducts();

    List<Order> getAllOrders();

    int getTodayOrderNumber();

    Order getOrder(long id);

    void updateOrder(Order order);
    
    List queryReport(String hql, Date from, Date to);
}
