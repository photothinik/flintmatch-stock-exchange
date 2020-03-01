package com.flintmatch.stockexchange.controller;

import com.flintmatch.stockexchange.config.ApplicationConfiguration;
import com.flintmatch.stockexchange.config.ApplicationProperty;
import com.flintmatch.stockexchange.config.ApplicationVersion;
import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.repository.impl.SqlPostgresOrderRepository;
import com.flintmatch.stockexchange.service.OrderMatchException;
import com.flintmatch.stockexchange.service.OrderMatchService;
import com.flintmatch.stockexchange.service.UnableToFulfillException;
import com.flintmatch.stockexchange.service.impl.SimpleOrderMatchService;
import com.flintmatch.stockexchange.tools.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StockExchangeController implements Runnable {

    // Logging
    private static final Logger logger = LoggerFactory.getLogger(StockExchangeController.class);
    private static ApplicationConfiguration config;

    // Controller
    private boolean done = false;

    // Service
    private OrderMatchService orderMatchService;

    // Repositories
    private OrderRepository orderRepository;



    public void run() {

        /* ++++++++++++ INITIALIZE ++++++++++ */

        logger.info("Match-Making Service v{} is starting", ApplicationVersion.getFullVersion());

        // Load config
        try {
            File configSrcFile = new File("config/stockexchange.config");
            logger.info("Loading configuration file '{}'", configSrcFile.getAbsolutePath());
            config = new ApplicationConfiguration(configSrcFile);
            config.loadFromSource();
        } catch(IOException e) {
            throw new RuntimeException("Unable to load application configuration: " + e.getMessage(), e);
        }

        // Setup Datasource
        logger.info("Configuring datasource");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config.getProperty(ApplicationProperty.REPOSITORY_DATASOURCE_DRIVERCLASS));
        dataSource.setUrl(config.getProperty(ApplicationProperty.REPOSITORY_DATASOURCE_URL));
        dataSource.setUsername(config.getProperty(ApplicationProperty.REPOSITORY_DATASOURCE_USERNAME));
        dataSource.setPassword(config.getProperty(ApplicationProperty.REPOSITORY_DATASOURCE_PASSWORD));

        // Repos
        logger.info("Configuring respositories");
        this.orderRepository = new SqlPostgresOrderRepository();
        this.orderRepository.setDataSource(dataSource);


        // Services
        logger.info("Configuring services");
        this.orderMatchService = new SimpleOrderMatchService(this.orderRepository);


        /* ++++++++++++ MAIN LOOOOP ++++++++++ */
        while(!this.done) {


            try {
                // Get sell orders
                List<Order> sellOrders = this.orderMatchService.getSellers();

                // For each sell order, search for buyers
                for(Order sellOrder : sellOrders) {

                    if( sellOrder.getFulfilled() == null)
                    {
                        logger.warn("Sell order '" +
                                Helper.getHumanReadableDescription(sellOrder) +
                                "' had invalid fulfilled state: skipping evaluation");
                        continue;
                    }

                    // Get all potential buyers
                    List<Order> potentialBuyers = this.orderMatchService.searchBuyers(sellOrder);
                    logger.info("Found {} potential buyer(s) for sell order {}", potentialBuyers.size(), Helper.getHumanReadableDescription(sellOrder));

                    // For each potential buyer, try to set a tentative fulfillment
                    for(Order potentialBuyer : potentialBuyers) {

                        // Only continue searching if the seller is not already fulfilled
                        if( sellOrder.getFulfilled().booleanValue())
                            break;

                        // Try to fulfill
                        try {
                            this.orderMatchService.fulfillTrade(potentialBuyer, sellOrder);
                            logger.info("Buy order {} successfully matched to sell order {}", Helper.getHumanReadableDescription(potentialBuyer), Helper.getHumanReadableDescription(sellOrder));
                        } catch(UnableToFulfillException e) {
                            // Fulfillment failed, go on to next potential buyer
                            logger.info("Buy order {} unable to be matched: {}", Helper.getHumanReadableDescription(potentialBuyer), e.getMessage());
                        }


                    }


                }

            } catch (OrderMatchException e) {

                logger.error("Exception while processing matching engine: " + e.getMessage(), e);

                // Extra pause
                Helper.safeSleep(Integer.valueOf(config.getProperty(ApplicationProperty.APPLICATION_MAIN_LOOP_ERROR_DELAY)));

            }


            // Save CPU cycles
            Helper.safeSleep(Integer.valueOf(config.getProperty(ApplicationProperty.APPLICATION_MAIN_LOOP_DELAY)));

        }

    }
}
