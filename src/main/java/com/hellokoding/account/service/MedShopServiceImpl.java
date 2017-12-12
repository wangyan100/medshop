/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hellokoding.account.service;

import com.hellokoding.account.model.Order;
import com.hellokoding.account.model.OrderDetail;
import com.hellokoding.account.model.Product;
import com.hellokoding.account.model.ShoppingCart;
import com.hellokoding.account.repository.MedShopDao;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yw
 */
@Service
public class MedShopServiceImpl implements MedShopService {

    @Autowired
    private MedShopDao medShopDao;

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return medShopDao.getAllProducts();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void addProductToShoppingCart(ShoppingCart shoppingCart, String pzn, int amount) {
        boolean found = false;
        if (shoppingCart.getOrders() == null) {
            shoppingCart.setOrders(new HashMap<Product, Integer>());
        }

        for (Map.Entry<Product, Integer> entry : shoppingCart.getOrders().entrySet()) {
            Product product = entry.getKey();
            if (product.getPzn().equalsIgnoreCase(pzn)) {
                found = true;
                shoppingCart.getOrders().put(product, entry.getValue() + amount);
            }
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        if (found == false) {
            //
            Product product = medShopDao.getProductByPZN(pzn);
            shoppingCart.getOrders().put(product, amount);
        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeProductNumberAtShoppingCart(ShoppingCart shoppingCart, String pzn, int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Product product = null;
        boolean remove = false;
        for (Map.Entry<Product, Integer> entry : shoppingCart.getOrders().entrySet()) {
            product = entry.getKey();
            if (product.getPzn().equalsIgnoreCase(pzn)) {

                if ((entry.getValue() + i) <= 0) {
                    remove = true;
                    break;
                    //shoppingCart.getOrders().remove(entry.getKey());
                } else {
                    shoppingCart.getOrders().put(product, entry.getValue() + i);
                }
                // shoppingCart.getOrders().put(product, entry.getValue() + 1);
            }
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        if (remove) {
            shoppingCart.getOrders().remove(product);
        }

    }

    @Override
    @Transactional
    public void makeOrder(ShoppingCart shoppingCart) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        //create a order
        Order order = new Order();

        order.setCreater(shoppingCart.getOrderCreator());
        order.setOrderdate(new java.sql.Date(System.currentTimeMillis()));
        order.setPickupDate(new java.sql.Date(shoppingCart.getPickupDate().getTime()));
        order.setPickupTime(shoppingCart.getPickupTime());
        order.setShopname(shoppingCart.getShopName());
        order.setTourGuideID(shoppingCart.getTourGuideID());
        order.setTourGuideName(shoppingCart.getTourGuideName());
        order.setTouristName(shoppingCart.getTouristName());
        float totalPrice = 0.00f;
        order.setOrderdetails(new ArrayList<OrderDetail>());
        for (Map.Entry<Product, Integer> entry : shoppingCart.getOrders().entrySet()) {
            Product product = entry.getKey();
            int amount = entry.getValue();
            float price = Float.parseFloat(product.getPrice());
            totalPrice = (price * amount) + totalPrice;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setChineseName(product.getChineseName());
            orderDetail.setGermanName(product.getGermanName());
            orderDetail.setOrder(order);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setPzn(product.getPzn());
            orderDetail.setUnit(product.getUnit());
            orderDetail.setAmount(amount);

            order.getOrderdetails().add(orderDetail);

        }
        order.setTotalPrice(totalPrice);
        //for (Product product : shoppingCart.getOrders())
        order.setStatus("VALID");

        int number = medShopDao.getTodayOrderNumber();

        String orderNumber = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "-" + String.format("%03d", number + 1);
        order.setOrderNumber(orderNumber);

        medShopDao.saveOrder(order);

        //
        // order.
    }

    @Override
    @Transactional
    public List<Order> getAllOrders() {

        return medShopDao.getAllOrders();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public Order getOrder(long id) {
        return medShopDao.getOrder(id);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void setOrderInvalid(long id) {

        Order order = medShopDao.getOrder(id);
        order.setStatus("INVALID");
        medShopDao.updateOrder(order);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public void addProductToOrder(long orderid, String pzn, int amount) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Order order = medShopDao.getOrder(orderid);

        OrderDetail foundOrderDetail = null;
        for (OrderDetail orderDetail : order.getOrderdetails()) {
            // 
            if (orderDetail.getPzn().equalsIgnoreCase(pzn)) {
                foundOrderDetail = orderDetail;
            }
        }

        if (foundOrderDetail == null) {
            float increasePrice = 0.0f;
            //it is a new one
            Product product = medShopDao.getProductByPZN(pzn);

            if (product == null) {
                return;
            }

            foundOrderDetail = new OrderDetail();
            foundOrderDetail.setAmount(amount);
            foundOrderDetail.setChineseName(product.getChineseName());
            foundOrderDetail.setGermanName(product.getGermanName());
            foundOrderDetail.setPzn(product.getPzn());
            foundOrderDetail.setPrice(product.getPrice());
            foundOrderDetail.setUnit(product.getUnit());
            foundOrderDetail.setOrder(order);

            float price = Float.parseFloat(product.getPrice());
            increasePrice = (price * amount) + increasePrice;
            medShopDao.saveOrderDetail(foundOrderDetail);
            order.setTotalPrice(order.getTotalPrice() + increasePrice);
            medShopDao.updateOrder(order);

        } else {
            //it is a update for existing one
            float increasePrice = 0.0f;
            float price = Float.parseFloat(foundOrderDetail.getPrice());
            increasePrice = (price * amount) + increasePrice;
            foundOrderDetail.setAmount(foundOrderDetail.getAmount() + amount);
            medShopDao.updateOrderDetail(foundOrderDetail);
            order.setTotalPrice(order.getTotalPrice() + increasePrice);
            medShopDao.updateOrder(order);

        }
    }

    @Override
    @Transactional
    public void removeProductFromOrder(long orderid, String pzn, int amount) {

        //get OrderDetail by PZN ,
        Order order = medShopDao.getOrder(orderid);

        OrderDetail foundOrderDetail = null;
        for (OrderDetail orderDetail : order.getOrderdetails()) {
            // 
            if (orderDetail.getPzn().equalsIgnoreCase(pzn)) {
                foundOrderDetail = orderDetail;
            }
        }

        //if not found, do nothing
        if (foundOrderDetail != null && (foundOrderDetail.getAmount() >= amount)) {

            //it is a update for existing one
            float price = Float.parseFloat(foundOrderDetail.getPrice());

            if (foundOrderDetail.getAmount() > amount) {
                //update this OrderDetail
                foundOrderDetail.setAmount(foundOrderDetail.getAmount() - amount);
                medShopDao.updateOrderDetail(foundOrderDetail);

            }

            if (foundOrderDetail.getAmount() == amount) {
                //remove this OrderDetail
                medShopDao.deleteOrderDetail(foundOrderDetail.getId());

            }


            order.setTotalPrice(order.getTotalPrice() - (price * amount));
            medShopDao.updateOrder(order);

        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional
    public List queryReport(String sql, Date fromDate, Date toDate) {
        
       return medShopDao.queryReport(sql, fromDate, toDate);
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
