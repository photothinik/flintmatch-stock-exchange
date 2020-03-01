package com.flintmatch.stockexchange.model;

public enum OrderType {

    BUY {
        public String getRepositoryCode() {
            return "B";
        }
    },
    SELL{
        public String getRepositoryCode() {
            return "S";
        }
    };

    public abstract String getRepositoryCode();

    public static OrderType forRepositoryCode(String code) {

        if( "B".equalsIgnoreCase(code))
            return BUY;
        else if( "S".equalsIgnoreCase(code))
            return SELL;
        else
            throw new IllegalArgumentException("Unable to find order type for code '" + code + "'");

    }
}
