package com.flintmatch.stockexchange.service;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderTransaction;

import java.util.List;

public interface OrderMatchService {

    List<Order> getUnfulfilledSellers() throws OrderMatchException;

    List<Order> getUnfulfilledBuyers() throws OrderMatchException;

    List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException;

    List<OrderTransaction> getOrderTransactions(Order o) throws OrderMatchException;

    void reserve(Order buyer, Order Seller, int quantity) throws UnableToFulfillException;

    void validateForCommit(OrderTransaction o) throws OrderMatchException;

    void commit(OrderTransaction o) throws UnableToFulfillException;

    void fulfill(Order order) throws UnableToFulfillException;

    int countReservedQuantity(List<OrderTransaction> orderTransactions);

    int countCommittedQuantity(List<OrderTransaction> orderTransactions);

    int countOrderQuantityAvailability(Order order, List<OrderTransaction> orderTransactions);

}
