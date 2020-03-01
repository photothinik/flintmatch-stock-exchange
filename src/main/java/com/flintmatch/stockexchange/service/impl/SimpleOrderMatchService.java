package com.flintmatch.stockexchange.service.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderType;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.service.OrderMatchException;
import com.flintmatch.stockexchange.service.OrderMatchService;
import com.flintmatch.stockexchange.service.UnableToFulfillException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleOrderMatchService implements OrderMatchService {

    private OrderRepository orderRepository;

    public SimpleOrderMatchService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException {

        // Buyers looking for the same stock symbol, and that are available

        List<Order> resultPotentialBuyers = new ArrayList<Order>();

        try {

            // Get all buyers

            // Filter
            Order buyerFilter = new Order();
            buyerFilter.setOrderType(OrderType.BUY);

            // Fetch
            List<Order> allBuyOrders = this.orderRepository.selectOrders(buyerFilter);

            // Check each buyer
            for(Order buyer : allBuyOrders) {

                if( buyer.getFulfilled() != null && buyer.getFulfilled().booleanValue())
                    continue;
                if( buyer.getConfirmed() != null && buyer.getConfirmed().booleanValue())
                    continue;
                if( buyer.getStockSymbol() == null || !buyer.getStockSymbol().equalsIgnoreCase(sellerOrder.getStockSymbol()))
                    continue;

                resultPotentialBuyers.add(buyer);

            }


        } catch (SQLException e) {
            throw new OrderMatchException("Searching for buyers failed: " + e.getMessage(), e);
        }

        return resultPotentialBuyers;

    }

    public void fulfillTrade(Order buyer, Order Seller) throws UnableToFulfillException {



        throw new UnableToFulfillException("Not implemented");
    }

    public List<Order> getSellers() throws OrderMatchException {
        try {

            // Create filter
            Order filter = new Order();
            filter.setOrderType(OrderType.SELL);

            // Run query
            return this.orderRepository.selectOrders(filter);

        } catch (SQLException e) {
            throw new OrderMatchException("Unable to search sellers: " + e.getMessage(), e);
        }

    }
}
