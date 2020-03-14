package com.flintmatch.stockexchange.tools;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderTransaction;

public class Helper {

    public static String getHumanReadableDescription(Order order) {

        return "Trader #" + order.getTraderId() + ", " + order.getOrderType().name() + "ING " + order.getTotalQuantity() + " shares of " + order.getStockSymbol();
    }

    public static String getHumanReadableDescription(OrderTransaction o) {

        return "Order transaction between buyer #" + o.getBuyerId() + " and seller # " + o.getSellerId() + " for " + o.getQuantity() + " units [" +
                (o.isReserved() ? "R" : "") + (o.isCommitted() ? "C" : "") + "]";
    }

    public static void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            /* Do Nothing */
        }
    }

}
