package com.flintmatch.stockexchange.repository.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderType;
import com.flintmatch.stockexchange.repository.OrderRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlPostgresOrderRepository implements OrderRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Order> selectOrders() throws SQLException {
        return selectOrders(null);
    }

    public List<Order> selectOrders(Order filter) throws SQLException {

        JdbcTemplate select = new JdbcTemplate(this.dataSource);

        // **********************************
        // Build conditions on the select
        // **********************************
        List<String> sqlConditions = new ArrayList<String>();
        List<Object> sqlParams = new ArrayList<Object>();

        if( filter != null ) {

            if (filter.getId() != null) {
                sqlConditions.add("id=?");
                sqlParams.add(filter.getId());
            }

            if (filter.getTraderId() != null) {
                sqlConditions.add("trader_id=?");
                sqlParams.add(filter.getTraderId());
            }

            if (filter.getOrderType() != null) {
                sqlConditions.add("order_type=?");
                sqlParams.add(filter.getOrderType().getRepositoryCode());
            }

            if (filter.getStockSymbol() != null) {
                sqlConditions.add("stock_symbol=?");
                sqlParams.add(filter.getStockSymbol());
            }

            if (filter.getQuantity() != null) {
                sqlConditions.add("quantity=?");
                sqlParams.add(filter.getQuantity());
            }

            if (filter.getFulfilled() != null) {
                sqlConditions.add("fulfilled=?");
                sqlParams.add(filter.getFulfilled() ? "Y" : "N");
            }

            if (filter.getConfirmed() != null) {
                sqlConditions.add("confirmed=?");
                sqlParams.add(filter.getConfirmed() ? "Y" : "N");
            }

        }

        // **********************************
        // Create query
        // **********************************

        StringBuffer sql = new StringBuffer("select * from order_transaction");

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
                new OrderRowMapper());
    }

    public void createOrder(Order o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to create order because the value passed was null");

        JdbcTemplate insert = new JdbcTemplate(this.dataSource);
        insert.update("insert into order_transaction " +
                        "(trader_id, order_type, stock_symbol, quantity, fulfilled, confirmed) values " +
                        "(?,?,?,?,?,?)",
                new Object[] {
                        o.getTraderId(),
                        o.getOrderType().getRepositoryCode(),
                        o.getStockSymbol(),
                        o.getQuantity(),
                        (o.getFulfilled() != null && o.getFulfilled().booleanValue() ? "Y" : "N"),
                        (o.getFulfilled() != null && o.getConfirmed().booleanValue() ? "Y" : "N")
                });
    }

    public void updateOrder(Order o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to update order because the value passed was null");
        if( o.getId() == null)
            throw new NullPointerException("Unable to update order because no order ID was found");

        JdbcTemplate update = new JdbcTemplate(this.dataSource);
        update.update(
                "update order_transaction set " +
                        "trader_id=?, " +
                        "order_type=?, " +
                        "stock_symbol=?, " +
                        "quantity=?, " +
                        "fulfilled=?, " +
                        "comfirmed=? " +
                        "where id=?",
                new Object[] {
                        o.getTraderId(),
                        o.getOrderType(),
                        o.getStockSymbol(),
                        o.getQuantity(),
                        (o.getFulfilled() != null && o.getFulfilled().booleanValue() ? "Y" : "N"),
                        (o.getConfirmed() != null && o.getConfirmed().booleanValue() ? "Y" : "N"),
                        o.getId()
                }
        );
    }

    public void deleteOrder(Order o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to delete order because the value passed was null");
        if( o.getId() == null)
            throw new NullPointerException("Unable to delete order because no order ID was found");

        JdbcTemplate delete = new JdbcTemplate(this.dataSource);
        delete.update(
                "delete * from order_transaction where id=?",
                new Object[] {o.getId()}
        );
    }

    /* ---------------------------------------------------------------------------- */

    private class OrderRowMapper implements RowMapper<Order> {
        public Order mapRow(ResultSet resultSet, int i) throws SQLException {
            return new OrderResultSetExtractor().extractData(resultSet);
        }
    }

    private class OrderResultSetExtractor implements ResultSetExtractor<Order> {
        public Order extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            return new Order(
                    resultSet.getLong("id"),
                    resultSet.getLong("trader_id"),
                    OrderType.forRepositoryCode(resultSet.getString("order_type")),
                    resultSet.getString("stock_symbol"),
                    resultSet.getInt("quantity"),
                    ("Y".equalsIgnoreCase(resultSet.getString("fulfilled")) ? Boolean.TRUE : Boolean.FALSE),
                    ("Y".equalsIgnoreCase(resultSet.getString("confirmed")) ? Boolean.TRUE : Boolean.FALSE)
            );
        }
    }
}
