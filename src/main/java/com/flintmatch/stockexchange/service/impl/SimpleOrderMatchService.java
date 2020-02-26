package com.flintmatch.stockexchange.service.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderType;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.service.OrderMatchException;
import com.flintmatch.stockexchange.service.OrderMatchService;

import java.sql.SQLException;
import java.util.List;

public class SimpleOrderMatchService implements OrderMatchService {

    private OrderRepository orderRepository;

    public SimpleOrderMatchService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException {
        throw new OrderMatchException("Not implemented");
    }

    public List<Order> searchSellers(Order buyerOrder) throws OrderMatchException {
        throw new OrderMatchException("Not implemented");
    }

    public void fulfillTrade(Order buyer, Order Seller) throws OrderMatchException {
        throw new OrderMatchException("Not implemented");
    }

    public List<Order> getBuyers() throws OrderMatchException {
        try {
            Order filter = new Order(null, null, OrderType.BUY, null, null);
            return this.orderRepository.selectOrders(filter);
        } catch (SQLException e) {
            throw new OrderMatchException("Unable to search buyers: " + e.getMessage(), e);
        }

    }

    public List<Order> getSellers() throws OrderMatchException {
        try {
            Order filter = new Order(null, null, OrderType.SELL, null, null);
            return this.orderRepository.selectOrders(filter);
        } catch (SQLException e) {
            throw new OrderMatchException("Unable to search sellers: " + e.getMessage(), e);
        }

    }
}
