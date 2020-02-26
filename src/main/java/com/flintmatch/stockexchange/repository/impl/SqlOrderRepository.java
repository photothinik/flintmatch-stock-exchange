package com.flintmatch.stockexchange.repository.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class SqlOrderRepository implements OrderRepository {

    private DataSource dataSource;

    public SqlOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Order> selectOrders() throws SQLException {
        throw new SQLException("Not implemented");
    }

    public List<Order> selectOrders(Order filter) throws SQLException {
        throw new SQLException("Not implemented");
    }

    public void createOrder(Order o) throws SQLException {
        throw new SQLException("Not implemented");
    }

    public void updateOrder(Order o) throws SQLException {
        throw new SQLException("Not implemented");
    }

    public void deleteOrder(Order o) throws SQLException {
        throw new SQLException("Not implemented");
    }
}
