package com.flintmatch.stockexchange.service.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderTransaction;
import com.flintmatch.stockexchange.model.OrderType;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.repository.OrderRepositoryOrderFilter;
import com.flintmatch.stockexchange.repository.OrderTransactionRepository;
import com.flintmatch.stockexchange.repository.OrderTransactionRepositoryFilter;
import com.flintmatch.stockexchange.service.OrderMatchException;
import com.flintmatch.stockexchange.service.OrderMatchService;
import com.flintmatch.stockexchange.service.UnableToFulfillException;
import com.flintmatch.stockexchange.tools.Helper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleOrderMatchService implements OrderMatchService {

    private OrderRepository orderRepository;
    private OrderTransactionRepository orderTransactionRepository;

    public SimpleOrderMatchService(OrderRepository orderRepository, OrderTransactionRepository orderTransactionRepository) {
        this.orderRepository = orderRepository;
        this.orderTransactionRepository = orderTransactionRepository;
    }

    public List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException {

        // Buyers looking for the same stock symbol, and that are available

        List<Order> resultPotentialBuyers = new ArrayList<Order>();

        try {

            // Get all buyers

            // Filter
            OrderRepositoryOrderFilter buyerFilter = new OrderRepositoryOrderFilter();
            buyerFilter.setOrderType(OrderType.BUY);
            buyerFilter.setFulfilled(false);

            // Fetch
            List<Order> allBuyOrders = this.orderRepository.selectOrders(buyerFilter);

            // Check each buyer
            for(Order buyer : allBuyOrders) {

                if( buyer.isFulfilled())
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

    public List<OrderTransaction> getOrderTransactions(Order o) throws OrderMatchException {

        try {

            OrderTransactionRepositoryFilter filter = new OrderTransactionRepositoryFilter();
            if( o.getOrderType() == OrderType.SELL)
                filter.setSellerId(o.getId());
            else if( o.getOrderType() == OrderType.BUY)
                filter.setBuyerId(o.getId());

            return this.orderTransactionRepository.selectOrderTransactions(filter);

        } catch (SQLException e) {
            throw new OrderMatchException("Getting for order transactions for order '"+ Helper.getHumanReadableDescription(o)+"' failed: " + e.getMessage(), e);
        }
    }

    public void reserve(Order buyer, Order seller, int quantity) throws UnableToFulfillException {

        try {
            OrderTransaction transaction = new OrderTransaction(seller.getId(), buyer.getId(), quantity, true, false);

            this.orderTransactionRepository.createOrder(transaction);

        } catch (SQLException e) {
            throw new UnableToFulfillException("Unable to reserve transaction between buyer '"+
                    Helper.getHumanReadableDescription(buyer)
                    +"' and seller '" +
                    Helper.getHumanReadableDescription(seller)
                    +"': " + e.getMessage(), e);
        }


    }

    public void validateForCommit(OrderTransaction o) throws OrderMatchException {

        // Validate that this order transaction can commit todo


    }

    public void commit(OrderTransaction o) throws UnableToFulfillException {
        try {
            o.setReserved(true);
            o.setCommitted(true);

            this.orderTransactionRepository.updateOrder(o);

        } catch (SQLException e) {
            throw new UnableToFulfillException("Unable to commit transaction '"+
                    Helper.getHumanReadableDescription(o)
                    +"': " + e.getMessage(), e);
        }
    }

    public void fulfill(Order order) throws UnableToFulfillException {
        try {
            order.setFulfilled(true);

            this.orderRepository.updateOrder(order);

        } catch (SQLException e) {
            throw new UnableToFulfillException("Unable to fulfill order '"+
                    Helper.getHumanReadableDescription(order)
                    +"': " + e.getMessage(), e);
        }

    }

    public int countReservedQuantity(List<OrderTransaction> orderTransactions) {
        int result = 0;

        if( orderTransactions != null ) {
            for(OrderTransaction ot : orderTransactions) {

                if( ot.isReserved() )
                    result+= ot.getQuantity().intValue();

            }
        }

        return result;
    }

    public int countCommittedQuantity(List<OrderTransaction> orderTransactions) {
        int result = 0;

        if( orderTransactions != null ) {
            for(OrderTransaction ot : orderTransactions) {

                if( ot.isCommitted() )
                    result+= ot.getQuantity().intValue();

            }
        }

        return result;

    }

    public int countOrderQuantityAvailability(Order order, List<OrderTransaction> orderTransactions) {
        int orderQuantityAvailable = order.getTotalQuantity();

        if( orderTransactions != null) {
            for(OrderTransaction ot : orderTransactions) {

                if(ot.isReserved() || ot.isCommitted())
                    orderQuantityAvailable-= ot.getQuantity();

            }
        }

        return orderQuantityAvailable;
    }

    public List<Order> getUnfulfilledSellers() throws OrderMatchException {
        return getUnfulfilledOrders(OrderType.SELL);
    }

    public List<Order> getUnfulfilledBuyers() throws OrderMatchException {
        return getUnfulfilledOrders(OrderType.BUY);
    }

    private List<Order> getUnfulfilledOrders(OrderType t) throws OrderMatchException {
        try {

            // Create filter
            OrderRepositoryOrderFilter filter = new OrderRepositoryOrderFilter();
            filter.setOrderType(t);
            filter.setFulfilled(false);

            // Run query
            return this.orderRepository.selectOrders(filter);

        } catch (SQLException e) {
            throw new OrderMatchException("Unable to search sellers: " + e.getMessage(), e);
        }

    }
}
