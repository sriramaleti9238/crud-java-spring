package org.example.mvc.controller;

import org.example.mvc.repository.entity.Order;
import org.example.mvc.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(value = "/order")  --> wrong declaration of path
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    private OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/allorders")
    public ResponseEntity<?> getAllOrder() {
        Optional<List<Order>> obj = Optional.ofNullable(orderService.findAll());
        if (obj.isPresent()){
            return ResponseEntity.ok(obj.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("list is empty");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        Optional<Order> order = Optional.ofNullable(orderService.findById(id));
        if (order.isPresent()){
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("order with id "+id+ " not found");
    }

    @PostMapping("/addData")
    public ResponseEntity<?> insertOrder(@RequestBody Order order) {
        Optional<Order> isOrderPresent = Optional.ofNullable(orderService.findById(order.getId()));
        if(isOrderPresent.isEmpty()){
            Order order2 = orderService.insertOrder(order);
            return ResponseEntity.ok(order2);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Order with id "+order.getId() + " already exists");
    }


//    @PutMapping("/{id}") -- wrong declaration
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order ) {
        Optional<Order> isOrderPresent = Optional.ofNullable(orderService.findById(id));
        if(isOrderPresent.isPresent()){
            order = orderService.update(id, order);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
        }
        String errorMessage = "order with ID "+ id + " not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        Optional<Order> isOrderPresent = Optional.ofNullable(orderService.findById(id));
        if(isOrderPresent.isPresent()){
            orderService.delete(id);
            return ResponseEntity.ok("Order deleted successfull "+id);
        }

        String errorMessage = "order with ID "+ id + " not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }


}
