package org.example.mvc.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.example.mvc.repository.entity.Order;
import org.example.mvc.repository.impl.OrderRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTests {



    @MockBean
    private OrderRepositoryImpl orderRepository;


    @BeforeEach
    public void setUp(){
        orderRepository =new OrderRepositoryImpl();

    }
    @Test
    public void findAllTest(){
        orderRepository.save(new Order(1561L, 5));
        orderRepository.save(new Order(1431L, 6));
        orderRepository.save(new Order(1565L, 8));

        // Call the findAll method
        List<Order> orders = orderRepository.findAll();

        // Verify the number of orders returned
        assertEquals(3, orders.size());
    }


    @Test
    public void findByIdTest(){
        orderRepository.save(new Order(1561L, 5));
        Order order=orderRepository.findById(1561L);
        assertEquals(order.getPrice(),5);
    }

    @Test
    public void saveTest(){
        Order newOrder = new Order(1000L, 10);
        Order savedOrder = orderRepository.save(newOrder);
        assertEquals(newOrder, savedOrder);
    }

    @Test
    public void deleteTest(){
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        Order order1 = new Order(1L, 1000);
        Order order2 = new Order(2L, 2000);
        Order order3 = new Order(3L,3000);
        Order order4 = new Order(4L,4000);
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);

        // Act
        orderRepository.delete(1L);
        orderRepository.delete(4L);
        //the order with id doesnot exist ,
        // to cover the branch test of the for loop\
        // we need to take the element which is not present in the list
        orderRepository.delete(6L);

        // Assert
        assertNull(orderRepository.findById(1L)); // Assuming findById returns null for non-existing orders
        assertNull(orderRepository.findById(4L));
        assertNotNull(orderRepository.findById(2L)); // Make sure the other order is still present
        assertEquals(orderRepository.findById(3L).getPrice(),3000);
        assertEquals(2, orderRepository.findAll().size()); // Make sure only one order is present after deletion





    }
}
