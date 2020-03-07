package com.flintmatch.stockexchange.model;

public class OrderTransaction {

    private Long sellerId;
    private Long buyerId;
    private Integer quantity;
    private boolean reserved = false;
    private boolean committed = false;

    public OrderTransaction(Long sellerId, Long buyerId, Integer quantity) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.quantity = quantity;
    }

    public OrderTransaction(Long sellerId, Long buyerId, Integer quantity, boolean reserved, boolean committed) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.quantity = quantity;
        this.reserved = reserved;
        this.committed = committed;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }
}
