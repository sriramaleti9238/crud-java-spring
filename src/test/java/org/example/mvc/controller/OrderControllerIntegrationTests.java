package org.example.mvc.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mvc.repository.entity.Order;
import org.example.mvc.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTests {
    @LocalServerPort
    private int port;
    private String baseUrl= "http://localhost:"+port+"/order";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;


    @Test
    public void insertOrderTest() throws Exception {

        Order order=new Order(30L,3456);
        Order order2=new Order(40L,2345);
        String url=baseUrl.concat("/addData");

        // order details with id exists
        Mockito.when(orderService.findById(30L)).thenReturn(order);
        Mockito.when(orderService.insertOrder(order)).thenReturn(order);

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                        .andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertEquals(responseBody,"Order with id 30 already exists");
        Mockito.verify(orderService,Mockito.times(0)).insertOrder(order);

        // when ordedr list with the id doesnot exist
        Mockito.when(orderService.findById(40L)).thenReturn(null);
        Mockito.when(orderService.insertOrder(order2)).thenReturn(order2);

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order2)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();


    }

    @Test
    public void getAllOrdersTest() throws Exception {

        Mockito.when(orderService.findAll())
                .thenReturn(List.of(new Order(1L,3245),new Order(2l,435)));

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/allorders"))
                        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Mockito.verify(orderService,Mockito.times(1)).findAll();
        List<Order> orderData =List.of(new Order(1L,3245),new Order(2l,435));

        assertEquals(responseBody, objectMapper.writeValueAsString(orderData));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/allorders"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Mockito.verify(orderService,Mockito.times(2)).findAll();

        // when list is empty
        Mockito.when(orderService.findAll())
                .thenReturn(null);

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/allorders"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String response= result1.getResponse().getContentAsString();

        assertEquals(response, "list is empty");

    }

    @Test
    public void updateOrderTest() throws Exception {
        Order order =new Order(6L,3452);
        Order updatingOrder =new Order(6L,5000);

        // when updating the order by id  exists
        when(orderService.findById(6L)).thenReturn(order);
        when(orderService.update(6L,updatingOrder)).thenReturn(updatingOrder);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/{id}",6L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatingOrder)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());


        // when updating the order by id doesnot exists
        when(orderService.findById(16L)).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/{id}",16L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatingOrder)))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String response= result.getResponse().getContentAsString();
        assertEquals(response,"order with ID 16 not found" );
    }

    @Test
    public void getOrderByIdTest() throws Exception{
        Order order =new Order(6L,5000);
        // when order is present with id
        when(orderService.findById(6L)).thenReturn(order);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/{id}",6L))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(response,objectMapper.writeValueAsString(order));
        verify(orderService,times(1)).findById(6L);


        // when order is not present with id
        when(orderService.findById(10L)).thenReturn(null);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/{id}",10L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        String response1 =result1.getResponse().getContentAsString();
        assertEquals(response1,"order with id 10 not found");
        verify(orderService,times(1)).findById(10L);

    }

    @Test
    public void deleteOrderTest() throws Exception{
        Order order=new Order(10L,5000);
        // when order is present with id
        when(orderService.findById(10L)).thenReturn(order);
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/{id}",10L))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String response=result.getResponse().getContentAsString();

        assertEquals(response, "Order deleted successfull 10");

        // when order is not present with id
        when(orderService.findById(20L)).thenReturn(null);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/{id}",20L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        String response1 =result1.getResponse().getContentAsString();
        assertEquals(response1,"order with ID 20 not found");
    }
}
