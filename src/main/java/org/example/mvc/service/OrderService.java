package org.example.mvc.service;


import org.example.mvc.repository.entity.Order;
import org.example.mvc.repository.impl.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepositoryImpl repository;

    public OrderService(OrderRepositoryImpl repository) {
        this.repository = repository;
    }

    public List<Order> findAll(){
        List<Order> obj = repository.findAll();
        return obj;
    }

    public Order insertOrder(Order order) {
        Order entity = new Order();
        entity.setId(order.getId());
        entity.setPrice(order.getPrice());
        return repository.save(entity);
    }

    public Order findById(Long id) {
        Order order = repository.findById(id);
        return order;
    }

    public Order update(Long id, Order order) {
        Order obj = repository.findById(id);
        /** deleting the existing data because
            the same data will be added in the list 2 times
            after save method called **/
        repository.delete(id);
        obj.setId(order.getId());
        obj.setPrice(order.getPrice());
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
