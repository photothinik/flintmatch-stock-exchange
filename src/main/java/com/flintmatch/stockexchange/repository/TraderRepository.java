package com.flintmatch.stockexchange.repository;

import com.flintmatch.stockexchange.model.Trader;

import java.sql.SQLException;
import java.util.List;

public interface TraderRepository {

    List<Trader> selectTraders() throws SQLException;

    List<Trader> selectTraders(Trader filter) throws SQLException;

    void createTrader(Trader t) throws SQLException;

    void updateTrader(Trader t) throws SQLException;

    void deleteTrader(Trader t) throws SQLException;

}
