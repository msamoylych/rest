package org.java.rest.service;

import org.java.rest.service.exception.TransferException;

import java.math.BigDecimal;

/**
 * Money transfer service
 */
public interface TransferService {

    /**
     * Transfer money from {@code from} account to {@code to} account
     *
     * @param from   Debit account
     * @param to     Credit account
     * @param amount Amount of money
     * @throws TransferException If {@code from} account or {@code to} account not found, or
     *                           there is not enough money on {@code from} account
     */
    void transfer(String from, String to, BigDecimal amount) throws TransferException;
}
