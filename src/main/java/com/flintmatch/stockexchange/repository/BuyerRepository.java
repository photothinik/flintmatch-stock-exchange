package com.flintmatch.stockexchange.repository;

import com.flintmatch.model.Requestor;
import com.flintmatch.repository.RequestorRepository;

import java.sql.SQLException;
import java.util.List;

public class BuyerRepository implements RequestorRepository {

    public List<Requestor> selectRequestors() throws SQLException {
        return null;
    }

    public void createRequestor(Requestor requestor) throws SQLException {

    }

    public void updateRequestor(Requestor requestor) throws SQLException {

    }

    public void deleteRequestor(Requestor requestor) throws SQLException {

    }
}
