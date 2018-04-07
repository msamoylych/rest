package org.java.rest.store;

import org.java.rest.store.dao.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory account store
 */
public class AccountStore {

    private static final Map<String, Account> STORAGE = new HashMap<>();

    static {
        STORAGE.put("1", new Account("1", new BigDecimal(100)));
        STORAGE.put("2", new Account("2", new BigDecimal(50)));
        STORAGE.put("3", new Account("3", new BigDecimal(10)));
        STORAGE.put("4", new Account("4", new BigDecimal(0)));
    }

    public Account get(String id) {
        return STORAGE.get(id);
    }
}
