package com.flintmatch.stockexchange.repository.impl;

import com.flintmatch.stockexchange.model.Order;
import com.flintmatch.stockexchange.model.OrderType;
import com.flintmatch.stockexchange.repository.OrderRepository;
import com.flintmatch.stockexchange.repository.OrderRepositoryOrderFilter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlPostgresOrderRepositoryImpl implements OrderRepository {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Order> selectOrders() throws SQLException {
        return selectOrders(null);
    }

    public List<Order> selectOrders(OrderRepositoryOrderFilter filter) throws SQLException {

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

            if (filter.getTotalQuantity() != null) {
                sqlConditions.add("total_quantity=?");
                sqlParams.add(filter.getTotalQuantity());
            }

            if (filter.getFulfilled() != null) {
                sqlConditions.add("fulfilled=?");
                sqlParams.add(filter.getFulfilled() ? "Y" : "N");
            }

        }

        // **********************************
        // Create query
        // **********************************

        StringBuffer sql = new StringBuffer("select * from stock_order");

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
        insert.update("insert into stock_order " +
                        "(trader_id, order_type, stock_symbol, total_quantity) values " +
                        "(?,?,?,?,?,?)",
                new Object[] {
                        o.getTraderId(),
                        o.getOrderType().getRepositoryCode(),
                        o.getStockSymbol(),
                        o.getTotalQuantity()
                });
    }

    public void updateOrder(Order o) throws SQLException {
        if(o == null)
            throw new NullPointerException("Unable to update order because the value passed was null");
        if( o.getId() == null)
            throw new NullPointerException("Unable to update order because no order ID was found");

        JdbcTemplate update = new JdbcTemplate(this.dataSource);
        update.update(
                "update stock_order set " +
                        "trader_id=?, " +
                        "order_type=?, " +
                        "stock_symbol=?, " +
                        "total_quantity=?, " +
                        "fulfilled=? " +
                        "where id=?",
                new Object[] {
                        o.getTraderId(),
                        o.getOrderType().getRepositoryCode(),
                        o.getStockSymbol(),
                        o.getTotalQuantity(),
                        (o.isFulfilled() ? "Y" : "N"),
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
                "delete * from stock_order where id=?",
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
                    resultSet.getInt("total_quantity"),
                    ("Y".equalsIgnoreCase(resultSet.getString("fulfilled")) ? Boolean.TRUE : Boolean.FALSE)
            );
        }
    }
}
