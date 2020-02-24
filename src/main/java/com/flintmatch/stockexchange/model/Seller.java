package com.flintmatch.stockexchange.model;

import com.flintmatch.model.Provider;

public class Seller implements Provider {
    public boolean isTransactionFulfilled() {
        return false;
    }

    public boolean isTransactionConfirmed() {
        return false;
    }
}
