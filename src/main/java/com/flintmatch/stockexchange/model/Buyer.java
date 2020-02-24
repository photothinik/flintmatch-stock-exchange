package com.flintmatch.stockexchange.model;

import com.flintmatch.model.Requestor;

public class Buyer implements Requestor {
    public boolean isRequestFulfilled() {
        return false;
    }

    public boolean isRequestConfirmed() {
        return false;
    }
}
