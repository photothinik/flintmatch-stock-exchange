package com.flintmatch.stockexchange.repository;

import com.flintmatch.model.Provider;
import com.flintmatch.repository.ProviderRepository;

import java.sql.SQLException;
import java.util.List;

public class SellerRepository implements ProviderRepository {
    public List<Provider> selectProviders() throws SQLException {
        return null;
    }

    public void createProvider(Provider provider) throws SQLException {

    }

    public void updateProvider(Provider provider) throws SQLException {

    }

    public void deleteProvider(Provider provider) throws SQLException {

    }
}
