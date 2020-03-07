package com.flintmatch.stockexchange.model;

public class Order {

    private Long id;
    private Long traderId;
    private OrderType orderType;
    private String stockSymbol;
    private Integer totalQuantity;
    private boolean fulfilled = false;

    public Order() {
    }

    public Order(Long id, Long traderId, OrderType orderType, String stockSymbol, Integer totalQuantity) {
        this.id = id;
        this.traderId = traderId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.totalQuantity = totalQuantity;
    }

    public Order(Long id, Long traderId, OrderType orderType, String stockSymbol, Integer totalQuantity, boolean fulfilled) {
        this.id = id;
        this.traderId = traderId;
        this.orderType = orderType;
        this.stockSymbol = stockSymbol;
        this.totalQuantity = totalQuantity;
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

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
