package com.flintmatch.stockexchange.repository;

import com.flintmatch.stockexchange.model.OrderType;

public class OrderRepositoryOrderFilter {

    private Long id;
    private Long traderId;
    private OrderType orderType;
    private String stockSymbol;
    private Integer quantity;
    private Boolean fulfilled;

    public OrderRepositoryOrderFilter() {
    }

    public OrderRepositoryOrderFilter(Long id, Long traderId, OrderType orderType, String stockSymbol, Integer quantity, Boolean fulfilled) {
        this.id = id;
        this.traderId = traderId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.fulfilled = fulfilled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }
}
