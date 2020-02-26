package com.flintmatch.stockexchange.repository;

import com.flintmatch.stockexchange.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderRepository {

    List<Order> selectOrders() throws SQLException;

    List<Order> selectOrders(Order filter) throws SQLException;

    void createOrder(Order o) throws SQLException;

    void updateOrder(Order o) throws SQLException;

    void deleteOrder(Order o) throws SQLException;

}
