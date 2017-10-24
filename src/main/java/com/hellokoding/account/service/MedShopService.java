/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.service;

import com.hellokoding.account.model.Order;
import com.hellokoding.account.model.Product;
import com.hellokoding.account.model.ShoppingCart;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author yw
 */
public interface MedShopService {
    
    List<Product> getAllProducts();
    
    void addProductToShoppingCart(ShoppingCart shoppingCart,String pzn);
    
    void changeProductNumberAtShoppingCart(ShoppingCart shoppingCart,String pzn, int i);
    
    void makeOrder(ShoppingCart shoppingCart);
    
    List<Order> getAllOrders();
    
    Order getOrder(long id);
    
    void setOrderInvalid(long id);
    
    void addProductToOrder(long orderid, String pzn, int amount);
    
    void removeProductFromOrder(long orderid,String pzn, int amount);
    
    List queryReport(String sql, Date fromDate , Date toDate);
    
    
    
}
