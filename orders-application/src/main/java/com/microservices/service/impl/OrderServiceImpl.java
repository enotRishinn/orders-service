package com.microservices.service.impl;

import com.microservices.database.DBHelper;
import com.microservices.model.AddItem;
import com.microservices.model.Order;
import com.microservices.service.OrderService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {
    private DBHelper dbHelper = new DBHelper();

    @Override
    public ArrayList<Order> getAllOrders() throws SQLException {
        return dbHelper.getAllOrders();
    }

    @Override
    public Order getOrderById(int id) throws SQLException {
        return dbHelper.getOrderById(id);
    }

    @Override
    public void changeOrderStatus(int id, String status) throws SQLException {
        dbHelper.changeOrderStatus(id, status);
    }

    @Override
    public void addItemToOrder(AddItem addItem) throws SQLException {
        dbHelper.addItemToOrder(addItem);
    }
}
