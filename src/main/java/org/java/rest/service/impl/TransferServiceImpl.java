package org.java.rest.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.java.rest.service.TransferService;
import org.java.rest.service.exception.TransferException;
import org.java.rest.store.AccountStore;
import org.java.rest.store.dao.Account;

import java.math.BigDecimal;

/**
 * Money transfer service implementation
 */
@Slf4j
public class TransferServiceImpl implements TransferService {

    private AccountStore accountStore;

    public TransferServiceImpl(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @Override
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void transfer(String from, String to, BigDecimal amount) throws TransferException {
        Account debit = getAccount(from);
        Account credit = getAccount(to);

        Object lock1, lock2;
        if (debit.getId().compareTo(credit.getId()) > 0) {
            lock1 = debit;
            lock2 = credit;
        } else {
            lock1 = credit;
            lock2 = debit;
        }

        synchronized (lock1) {
            synchronized (lock2) {
                BigDecimal debitAmount = debit.getAmount();
                BigDecimal creditAmount = credit.getAmount();
                if (debitAmount.compareTo(amount) < 0) {
                    throw new TransferException("Not enough money");
                }

                debit.setAmount(debitAmount.subtract(amount));
                credit.setAmount(creditAmount.add(amount));

                log.info("{} -> {} ({}) | {}: {} -> {} | {}: {} -> {}",
                        from, to, amount.toPlainString(),
                        from, debitAmount.toPlainString(), debit.getAmount().toPlainString(),
                        to, creditAmount.toPlainString(), credit.getAmount().toPlainString());
            }
        }
    }

    private Account getAccount(String id) throws TransferException {
        Account account = accountStore.get(id);
        if (account == null) {
            throw new TransferException("Account with id <" + id + "> not found");
        }
        return account;
    }
}
