package com.flintmatch.stockexchange.repository;

public class OrderTransactionRepositoryFilter {

    private Long sellerId;
    private Long buyerId;
    private Integer quantity;
    private Boolean reserved;
    private Boolean committed;

    public OrderTransactionRepositoryFilter() {
    }

    public OrderTransactionRepositoryFilter(Long sellerId, Long buyerId, Integer quantity, Boolean reserved, Boolean committed) {
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

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Boolean getCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }
}
