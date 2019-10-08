package com.microservices.database;

import com.microservices.model.AddItem;
import com.microservices.model.Address;
import com.microservices.model.Order;
import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.util.ArrayList;

public class DBHelper {
    private static final String ID = "id";
    private static final String BD_NAME = "orders";
    private static final String TABLE_NAME = "order";
    private static final String STATUS = "orderStatus";
    private static final String ITEM = "itemID";
    private static final String AMOUNT = "itemAmount";
    private static final String PRICE = "price";
    private static final String EMAIL = "email";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String HOUSE = "house";
    private static final String CORP = "corp";
    private static final String FLAT = "flat";


    private static final String URL = "jdbc:mysql://localhost:3306/" + BD_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static Connection connection;

    public void getConnection() {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to load driver class");
        }
    }

    public void startFormingOrder(int itemID, float price) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "INSERT INTO " + TABLE_NAME + " (" + STATUS + "," + ITEM + "," + AMOUNT + ";"+ PRICE + ") " +
                "VALUES (Collecting," + itemID + ",1;" + price + ");";
        statement.execute(sql);
        connection.close();
    }

    public void addInfoToOrder(int orderId, Address address) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + TABLE_NAME + " SET " + EMAIL + " = " + address.email + "," + COUNTRY + "=" +
                address.country + "," + CITY + "=" + address.city + "," + STREET + "=" + address.street + "," +
                HOUSE + "=" + address.house + "," + CORP + "=" + address.corp + "," + FLAT + "=" + address.flat +
                " WHERE " + ID + " = " + orderId + ";";
        statement.execute(sql);
        connection.close();
    }

    public void changeOrderStatus (int id, String status) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "UPDATE " + TABLE_NAME + " SET " + STATUS + "=" + status + " WHERE " + ID + "=" + id + ";";
        statement.execute(sql);
        connection.close();
    }

    private ArrayList<Order> getOrder(String sql) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<Order> orders= new ArrayList<>();

        if (rs != null) {
            while (rs.next()){
                Order order = new Order(rs.getInt(ID), rs.getString(STATUS), rs.getInt(ITEM), rs.getInt(AMOUNT),
                        rs.getFloat(PRICE), rs.getString(EMAIL), rs.getString(COUNTRY), rs.getString(CITY),
                        rs.getString(HOUSE), rs.getInt(STREET), rs.getInt(CORP), rs.getInt(FLAT));
                orders.add(order);
            }
        }
        connection.close();
        return orders;
    }

    public ArrayList<Order> getAllOrders() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        return getOrder(sql);
    }

    public Order getOrderById(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id + ";";
        return getOrder(sql).get(0);
    }

    public void addOneMoreItem(int orderId, int itemId) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT " + AMOUNT + " FROM " + TABLE_NAME + " WHERE " + ID + "=" + orderId +
                " || " + ITEM + "=" + itemId + ";";
        ResultSet resultSet = statement.executeQuery(sql);
        int newAmount = 0;
        float newPrice = 0;
        while (resultSet.next()){
            newAmount += resultSet.getInt(AMOUNT);
            newPrice +=resultSet.getFloat(PRICE);
        }
        newPrice = newPrice/newAmount;
        newAmount += 1;
        newPrice *= newAmount;
        sql = "UPDATE " + TABLE_NAME + " SET " + AMOUNT + " = " + newAmount + ", " + PRICE + " = " + newPrice +
                " WHERE " + ID + "=" + orderId + " || " + ITEM + "=" + itemId + ";";
        statement.execute(sql);
        connection.close();
    }


    public void addItemToOrder(AddItem addItem) throws SQLException {
        getConnection();
        int itemId = addItem.id;
        float price = addItem.price;
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM + " = " + itemId + " && " + STATUS + "= 'Collecting';";
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<Order> orders= new ArrayList<>();
        if (rs!=null){
            addOneMoreItem(itemId, rs.getInt(ID));
        } else {
            startFormingOrder(itemId, price);
        }
        connection.close();
    }
}
