package com.flintmatch.stockexchange;

import com.flintmatch.stockexchange.controller.StockExchangeController;

public class Application {

    public static void main(String[] args) {

        new Thread(new StockExchangeController()).start();

    }

}
