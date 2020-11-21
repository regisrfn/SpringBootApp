package com.rufino.server.services;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.OrderDAO;
import com.rufino.server.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderDAO orderDAO;

    @Autowired
    public OrderService (@Qualifier("DB_H2") OrderDAO orderDAO){
        this.orderDAO = orderDAO;
    }

    public int addOrder(Order order){
        return orderDAO.insertOrder(order);
    }

    public int delete(UUID id){
        return orderDAO.deleteOrder(id);
    }

    public List<Order> getAll(){
        return orderDAO.getAllOrder();
    }

    public Order getOrder(UUID id){
        return orderDAO.getOrder(id);
    }

    public int update(UUID id,Order order) {
        return orderDAO.updateOrder(id,order);
    }
    
}
