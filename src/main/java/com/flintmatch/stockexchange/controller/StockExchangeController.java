package com.flintmatch.stockexchange.controller;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.repository.impl.SqlOrderRepository;
import com.flintmatch.stockexchange.service.OrderMatchException;
import com.flintmatch.stockexchange.service.OrderMatchService;
import com.flintmatch.stockexchange.service.UnableToFulfillException;
import com.flintmatch.stockexchange.service.impl.SimpleOrderMatchService;

import java.util.List;

public class StockExchangeController implements Runnable {

    private boolean done = false;

    // Service
    private OrderMatchService orderMatchService;

    // Repositories
    private OrderRepository orderRepository;



    public void run() {

        /* ++++++++++++ INITIALIZE ++++++++++ */

        // Repos
        this.orderRepository = new SqlOrderRepository(null);

        // Services
        this.orderMatchService = new SimpleOrderMatchService(this.orderRepository);


        /* ++++++++++++ MAIN LOOOOP ++++++++++ */
        while(!this.done) {


            try {
                // Get sell orders
                List<Order> sellOrders = this.orderMatchService.getSellers();

                // For each sell order, search for buyers
                for(Order sellOrder : sellOrders) {
                    List<Order> potentialBuyers = this.orderMatchService.searchBuyers(sellOrder);

                    // For each potential buyer, try to set a tentative fulfillment
                    for(Order potentialBuyer : potentialBuyers) {

                        // Only continue searching if the seller is not already fulfilled
                        if( sellOrder.isFulfilled())
                            break;

                        // Try to fulfill
                        try {
                            this.orderMatchService.fulfillTrade(potentialBuyer, sellOrder);
                        } catch(UnableToFulfillException e) {
                            // Fulfillment failed, go on to next potential buyer
                        }


                    }


                }

            } catch (OrderMatchException e) {

                e.printStackTrace();

            }


            // Save CPU cycles
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Do nothing
            }

        }

    }
}
