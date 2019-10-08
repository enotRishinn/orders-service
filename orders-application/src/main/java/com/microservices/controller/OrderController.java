package com.microservices.controller;

import com.microservices.model.AddItem;
import com.microservices.model.Order;
import com.microservices.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderService;
    private static final Logger log = Logger.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ArrayList<Order> getAllOrders() throws SQLException {
        return orderService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addItemToOrder(@Valid @RequestBody AddItem addItem){
        try {
            orderService.addItemToOrder(addItem);
            log.info("Item with id = " + addItem.id + " added to cart");
        } catch (SQLException e) {
            log.error("Error adding product with id = " + addItem.id + " to cart: " + e.toString());
        }
    }

    @GetMapping(value = "/id/{id}")
    public Order getOrderById (@PathVariable int id) {
        try {
            Order temp = orderService.getOrderById(id);
            log.info("Order with id = " + id + " was found: " + temp.toString());
            return temp;
        } catch (SQLException e) {
            log.error("Order with id = " + id + "was not found: " + e.toString());
            return null;
        }
    }

    @PutMapping(value = "{id}/change/{status}")
    public void changeStatus(@PathVariable int id, @PathVariable String status) {
        try {
            orderService.changeOrderStatus(id, status);
            log.info("Order status replaced by " + status);
        } catch (SQLException e) {
            log.error("Error changing order status: " + e.toString());
        }
    }
}
