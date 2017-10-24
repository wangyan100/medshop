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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 *
 * @author yw
 */
@Repository
public class MedShaoDaoImpl implements MedShopDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product getProductByPZN(String pzn) {
        Product product = null;
        List<Product> list = entityManager.createQuery("SELECT p FROM Product p WHERE p.pzn=:pzn")
                .setParameter("pzn", pzn).getResultList();
        if (!list.isEmpty()) {
            product = list.get(0);
        }
        return product;
    }

    public void updateProduct(Product product) {
        entityManager.merge(product);
    }

    public void saveProduct(Product product) {
        entityManager.persist(product);
    }

    @Override
    public List<Product> getAllProducts() {

        //List list=null;
        String hql = "FROM Product as product ORDER BY product.product_id";
        return (List<Product>) entityManager.createQuery(hql).getResultList();

        //return list;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTodayOrderNumber() {

        String sql = "SELECT * FROM productorder WHERE DATE(orderdate) = CURDATE()";
        return entityManager.createNativeQuery(sql).getResultList().size();

    }

    @Override
    public void saveOrder(Order order) {
        entityManager.persist(order);
        for (OrderDetail orderDetail : order.getOrderdetails()) {
            entityManager.persist(orderDetail);
        }
    }

    @Override
    public List<Order> getAllOrders() {

        String hql = "FROM Order as order where order.status=:status ORDER BY order.id desc";
        return (List<Order>) entityManager.createQuery(hql).setParameter("status", "VALID").getResultList();

    }

    @Override
    public Order getOrder(long id) {

        Order order = null;
        List<Order> list = entityManager.createQuery("SELECT o FROM Order o WHERE o.id=:id")
                .setParameter("id", id).getResultList();
        if (!list.isEmpty()) {
            order = list.get(0);
        }
        return order;
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOrder(Order order) {
        entityManager.merge(order);
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {

        entityManager.persist(orderDetail);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateOrderDetail(OrderDetail orderDetail) {
        entityManager.merge(orderDetail);
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteOrderDetail(long orderdetailid) {

        entityManager.createQuery("delete  FROM OrderDetail  WHERE id=:id")
                .setParameter("id", orderdetailid).executeUpdate();
    }

    @Override
    public List queryReport(String sql, Date fromDate, Date toDate) {

        return entityManager.createNativeQuery(sql)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
