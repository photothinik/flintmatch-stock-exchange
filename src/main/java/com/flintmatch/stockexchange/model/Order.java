package com.flintmatch.stockexchange.model;

public class Order {

    private Long id;
    private Long traderId;
    private OrderType orderType;
    private String stockSymbol;
    private Integer quantity;
    private Boolean fulfilled;
    private Boolean confirmed;

    public Order() {
    }

    public Order(Long id, Long traderId, OrderType orderType, String stockSymbol, Integer quantity) {
        this.id = id;
        this.traderId = traderId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
    }

    public Order(Long id, Long traderId, OrderType orderType, String stockSymbol, Integer quantity, Boolean fulfilled, Boolean confirmed) {
        this.id = id;
        this.traderId = traderId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.fulfilled = fulfilled;
        this.confirmed = confirmed;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
