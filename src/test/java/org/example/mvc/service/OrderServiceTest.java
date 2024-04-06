package org.example.mvc.service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.mvc.repository.OrderRepository;
import org.example.mvc.repository.entity.Order;
import org.example.mvc.repository.impl.OrderRepositoryImpl;
import org.example.mvc.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepositoryImpl orderRepository;
    @InjectMocks
    OrderService orderService;

    Order order;
    List<Order> orderList=null;

    @BeforeEach
    void orderData(){
        order=new Order(12L, 3500);
        orderList = new ArrayList<>();
        orderList.add(new Order(1L, 200));
        orderList.add(new Order(3L, 600));
        orderList.add(order);
    }
    @Test
    void findAllTest(){
        when(orderRepository.findAll()).thenReturn(orderList);
        List<Order> orderData= orderService.findAll();
        assertEquals(orderData.size(),3);

    }
    @Test
    void findByIdTest(){
        when(orderRepository.findById(12L)).thenReturn(order);
        Order orderData = orderService.findById(12L);
        assertEquals(orderData.getPrice(),3500);
        String orderString= "Order{" + "id=" + orderData.getId() + ", price=" + orderData.getPrice() +
                '}';
        assertEquals(orderString,orderData.toString());

    }
    @Test
    void insertOrderTest(){
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order orderData= orderService.insertOrder(order);
        assertEquals(3500,orderData.getPrice());
    }
    @Test
    void updateTest(){
        when(orderRepository.save(order)).thenReturn(order);
        when(orderRepository.findById(12L)).thenReturn(order);
        Order orderData= orderService.update(12L,order);
        assertEquals(3500,orderData.getPrice());

    }
    @Test
    void deleteTest(){
        orderService.delete(12L);
        verify(orderRepository,times(1)).delete(12L);
    }
}
