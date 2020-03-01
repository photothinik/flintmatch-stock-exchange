package com.flintmatch.stockexchange.tools;

import com.flintmatch.stockexchange.model.Order;

public class Helper {

    public static String getHumanReadableDescription(Order order) {

        return "Trader #" + order.getTraderId() + ", " + order.getOrderType().name() + "ING " + order.getQuantity() + " shares of " + order.getStockSymbol();
    }

    public static void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            /* Do Nothing */
        }
    }

}
