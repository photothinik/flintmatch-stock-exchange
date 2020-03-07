package com.flintmatch.stockexchange.repository.impl;

import com.flintmatch.stockexchange.model.OrderTransaction;
import com.flintmatch.stockexchange.repository.OrderTransactionRepository;
import com.flintmatch.stockexchange.repository.OrderTransactionRepositoryFilter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlPostgresOrderTransactionRepositoryImpl implements OrderTransactionRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<OrderTransaction> selectOrderTransactions() throws SQLException {
        return selectOrderTransactions(null);
    }

    public List<OrderTransaction> selectOrderTransactions(OrderTransactionRepositoryFilter filter) throws SQLException {
        JdbcTemplate select = new JdbcTemplate(this.dataSource);

        // **********************************
        // Build conditions on the select
        // **********************************
        List<String> sqlConditions = new ArrayList<String>();
        List<Object> sqlParams = new ArrayList<Object>();

        if( filter != null ) {

            if (filter.getSellerId() != null) {
                sqlConditions.add("seller_id=?");
                sqlParams.add(filter.getSellerId());
            }

            if (filter.getBuyerId() != null) {
                sqlConditions.add("buyer_id=?");
                sqlParams.add(filter.getBuyerId());
            }

            if (filter.getQuantity() != null) {
                sqlConditions.add("quantity=?");
                sqlParams.add(filter.getQuantity());
            }

            if (filter.getReserved() != null) {
                sqlConditions.add("reserved=?");
                sqlParams.add(filter.getReserved() ? "Y" : "N");
            }

            if (filter.getCommitted() != null) {
                sqlConditions.add("committed=?");
                sqlParams.add(filter.getCommitted() ? "Y" : "N");
            }

        }

        // **********************************
        // Create query
        // **********************************

        StringBuffer sql = new StringBuffer("select * from stock_order_transaction");

        if( sqlConditions.size() > 0) {
            sql.append(" where ");
            String andOp = "";

            for(String condition : sqlConditions) {
                sql.append( andOp + condition);
                andOp = " and ";
            }
        }

        // **********************************
        // Run query
        // **********************************
        return select.query(
                sql.toString(),
                sqlParams.toArray(),
                new OrderTransactionRowMapper());
    }

    public void createOrder(OrderTransaction o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to create order transaction because the value passed was null");

        JdbcTemplate insert = new JdbcTemplate(this.dataSource);
        insert.update("insert into stock_order_transaction " +
                        "(seller_id, buyer_id, quantity, reserved, committed) values " +
                        "(?,?,?,?,?)",
                new Object[] {
                        o.getSellerId(),
                        o.getBuyerId(),
                        o.getQuantity(),
                        (o.isReserved() ? "Y" : "N"),
                        (o.isCommitted() ? "Y" : "N")
                });
    }

    public void updateOrder(OrderTransaction o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to update order transaction because the value passed was null");
        if( o.getSellerId() == null )
            throw new NullPointerException("Unable to update order transaction because no seller ID was found");
        if( o.getBuyerId() == null )
            throw new NullPointerException("Unable to update order transaction because no buyer ID was found");

        JdbcTemplate update = new JdbcTemplate(this.dataSource);
        update.update(
                "update stock_order_transaction set " +
                        "quantity=?, " +
                        "reserved=?, " +
                        "committed=? " +
                        "where seller_id=? and buyer_id=?",
                new Object[] {
                        o.getQuantity(),
                        (o.isReserved() ? "Y" : "N"),
                        (o.isCommitted() ? "Y" : "N"),
                        o.getSellerId(),
                        o.getBuyerId()
                }
        );
    }

    public void deleteOrder(OrderTransaction o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to update order transaction because the value passed was null");
        if( o.getSellerId() == null )
            throw new NullPointerException("Unable to update order transaction because no seller ID was found");
        if( o.getBuyerId() == null )
            throw new NullPointerException("Unable to update order transaction because no buyer ID was found");

        JdbcTemplate delete = new JdbcTemplate(this.dataSource);
        delete.update(
                "delete * from stock_order_transaction where seller_id=? and buyer_id=?",
                new Object[] {o.getSellerId(), o.getBuyerId()}
        );
    }


    /* ---------------------------------------------------------------------------- */

    private class OrderTransactionRowMapper implements RowMapper<OrderTransaction> {
        public OrderTransaction mapRow(ResultSet resultSet, int i) throws SQLException {
            return new OrderTransactionResultSetExtractor().extractData(resultSet);
        }
    }

    private class OrderTransactionResultSetExtractor implements ResultSetExtractor<OrderTransaction> {
        public OrderTransaction extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            return new OrderTransaction(
                    resultSet.getLong("seller_id"),
                    resultSet.getLong("buyer_id"),
                    resultSet.getInt("quantity"),
                    ("Y".equalsIgnoreCase(resultSet.getString("reserved")) ? Boolean.TRUE : Boolean.FALSE),
                    ("Y".equalsIgnoreCase(resultSet.getString("committed")) ? Boolean.TRUE : Boolean.FALSE)
            );
        }
    }

}
