package com.flintmatch.stockexchange.service;

import com.flintmatch.stockexchange.model.Order;

import java.util.List;

public interface OrderMatchService {

    List<Order> getSellers() throws OrderMatchException;

    List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException;

    void reserve(Order buyer, Order Seller) throws UnableToFulfillException;

}
