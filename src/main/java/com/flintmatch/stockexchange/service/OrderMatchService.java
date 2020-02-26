package com.flintmatch.stockexchange.service;

import com.flintmatch.stockexchange.model.Order;

import java.util.List;

public interface OrderMatchService {

    List<Order> getBuyers() throws OrderMatchException;

    List<Order> getSellers() throws OrderMatchException;

    List<Order> searchBuyers(Order sellerOrder) throws OrderMatchException;

    List<Order> searchSellers(Order buyerOrder) throws OrderMatchException;

    void fulfillTrade(Order buyer, Order Seller) throws UnableToFulfillException;

}
