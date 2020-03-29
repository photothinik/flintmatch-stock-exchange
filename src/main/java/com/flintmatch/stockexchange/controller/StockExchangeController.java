package com.flintmatch.stockexchange.controller;

import com.flintmatch.stockexchange.config.ApplicationConfiguration;
import com.flintmatch.stockexchange.config.ApplicationProperty;
import com.flintmatch.stockexchange.config.ApplicationVersion;
import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderTransaction;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.repository.OrderTransactionRepository;
import com.flintmatch.stockexchange.repository.impl.SqlPostgresOrderRepositoryImpl;
import com.flintmatch.stockexchange.repository.impl.SqlPostgresOrderTransactionRepositoryImpl;
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
    private OrderTransactionRepository orderTransactionRepository;



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
        this.orderRepository = new SqlPostgresOrderRepositoryImpl();
        this.orderRepository.setDataSource(dataSource);
        this.orderTransactionRepository = new SqlPostgresOrderTransactionRepositoryImpl();
        this.orderTransactionRepository.setDataSource(dataSource);


        // Services
        logger.info("Configuring services");
        this.orderMatchService = new SimpleOrderMatchService(this.orderRepository, this.orderTransactionRepository);


        /* ++++++++++++ MAIN LOOOOP ++++++++++ */
        while(!this.done) {


            try {
                // Get sell orders
                List<Order> sellOrders = this.orderMatchService.getUnfulfilledSellers();

                // For each sell order, search for buyers
                for(Order sellOrder : sellOrders) {

                    // Skip

                    // Get all potential buyers
                    List<Order> potentialBuyers = this.orderMatchService.searchBuyers(sellOrder);
                    logger.debug("Found {} potential buyer(s) for sell order {}", potentialBuyers.size(), Helper.getHumanReadableDescription(sellOrder));


                    /* ++++++++++++++++++ RESERVATION PHASE +++++++++++++++++++ */

                    for(Order potentialBuyer : potentialBuyers) {

                        List<OrderTransaction> sellerOrderTransactions = this.orderMatchService.getOrderTransactions(sellOrder);

                        // Here we check all buyer reservations to see if the seller's request is complete
                        if( this.orderMatchService.countReservedQuantity(sellerOrderTransactions) >= sellOrder.getTotalQuantity().intValue())
                            break;

                        // Get the oder transactions involved
                        List<OrderTransaction> buyerOrderTransactions = this.orderMatchService.getOrderTransactions(potentialBuyer);

                        // Calculate buy/sell order unit availability
                        int sellerUnitsAvailableForReserve = this.orderMatchService.countOrderQuantityAvailability(sellOrder, sellerOrderTransactions);
                        int buyerUnitsAvailableForReserve = this.orderMatchService.countOrderQuantityAvailability(potentialBuyer, buyerOrderTransactions);

                        if (buyerUnitsAvailableForReserve <= 0 )
                            continue;


                        // Determine the quantity we are trying to reserve - how much of the seller's quantity this buyer is going to take
                        int quantityToReserve = 0;



                        logger.info("Buyer {} has {} quantity available", Helper.getHumanReadableDescription(potentialBuyer), buyerUnitsAvailableForReserve);

                        if( sellerUnitsAvailableForReserve <= buyerUnitsAvailableForReserve )
                            quantityToReserve = sellerUnitsAvailableForReserve;
                        else
                            quantityToReserve = buyerUnitsAvailableForReserve;

                        // Try to reserve
                        try {
                            if(quantityToReserve == 0)
                                throw new UnableToFulfillException("Quantity to fulfill is 0");

                            this.orderMatchService.reserve(potentialBuyer, sellOrder, quantityToReserve);
                            logger.info("Buy order {} successfully reserved {} units for sell order {}", Helper.getHumanReadableDescription(potentialBuyer), quantityToReserve, Helper.getHumanReadableDescription(sellOrder));
                        } catch(UnableToFulfillException e) {
                            // Fulfillment failed, go on to next potential buyer
                            logger.info("Buy order {} unable to be reserved: {}", Helper.getHumanReadableDescription(potentialBuyer), e.getMessage());
                        }


                    }


                    /* ++++++++++++++++++ COMMIT PHASE +++++++++++++++++++ */

                    List<OrderTransaction> sellerOrderTransactions = this.orderMatchService.getOrderTransactions(sellOrder);

                    // See if any of the reserved buy transactions can committed now
                    for(OrderTransaction currentOrderTransaction : sellerOrderTransactions) {

                        try {
                            // Validate
                            this.orderMatchService.validateForCommit(currentOrderTransaction);

                            // Commit
                            this.orderMatchService.commit(currentOrderTransaction);

                        } catch (UnableToFulfillException e) {
                            logger.info("Unable to commit order transaction '{}': {}", Helper.getHumanReadableDescription(currentOrderTransaction), e.getMessage());
                        } catch (OrderMatchException e) {
                            logger.info("Unable to validate order transaction '{}' for commit: {}", Helper.getHumanReadableDescription(currentOrderTransaction), e.getMessage());
                        }

                    }

                    /* ++++++++++++++++++ FULFILL SELLER PHASE +++++++++++++++++++ */

                    // See if the seller's order is complete and can be fulfilled
                    if( this.orderMatchService.countCommittedQuantity(sellerOrderTransactions) == sellOrder.getTotalQuantity() ) {
                        try {
                            this.orderMatchService.fulfill(sellOrder);
                            logger.info("Sell order '{}' has been fulfilled", Helper.getHumanReadableDescription(sellOrder));
                        } catch (UnableToFulfillException e) {
                            logger.info("Sell order {} unable to be fulfilled: {}", Helper.getHumanReadableDescription(sellOrder), e.getMessage());
                        }

                    }



                }

                /* ++++++++++++++++++ FULFILL BUYER PHASE +++++++++++++++++++ */
                List<Order> unfulfilledBuyers = this.orderMatchService.getUnfulfilledBuyers();
                for (Order buyer : unfulfilledBuyers) {

                    List<OrderTransaction> buyerOrderTransactions = this.orderMatchService.getOrderTransactions(buyer);

                    if( this.orderMatchService.countCommittedQuantity(buyerOrderTransactions) == buyer.getTotalQuantity() ) {
                        try {
                            this.orderMatchService.fulfill(buyer);
                            logger.info("Buy order '{}' has been fulfilled", Helper.getHumanReadableDescription(buyer));
                        } catch (UnableToFulfillException e) {
                            logger.info("Buy order {} unable to be fulfilled: {}", Helper.getHumanReadableDescription(buyer), e.getMessage());
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
