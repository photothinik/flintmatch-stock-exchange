package com.flintmatch.stockexchange.repository;

import com.flintmatch.stockexchange.model.OrderTransaction;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface OrderTransactionRepository {

    void setDataSource(DataSource dataSource);

    List<OrderTransaction> selectOrderTransactions() throws SQLException;

    List<OrderTransaction> selectOrderTransactions(OrderTransactionRepositoryFilter filter) throws SQLException;

    void createOrder(OrderTransaction ot) throws SQLException;

    void updateOrder(OrderTransaction ot) throws SQLException;

    void deleteOrder(OrderTransaction ot) throws SQLException;


}
