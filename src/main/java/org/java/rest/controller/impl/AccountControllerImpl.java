package org.java.rest.controller.impl;

import org.java.rest.controller.AccountController;
import org.java.rest.controller.dto.Transfer;
import org.java.rest.service.TransferService;
import org.java.rest.service.exception.TransferException;

import javax.ws.rs.core.Response;

public class AccountControllerImpl implements AccountController {

    private TransferService transferService;

    public AccountControllerImpl(TransferService transferService) {
        this.transferService = transferService;
    }

    public Response transfer(Transfer transfer) {
        try {
            transferService.transfer(transfer.getFrom(), transfer.getTo(), transfer.getAmount());
            return Response.ok().build();
        } catch (TransferException ex) {
            return Response.status(422).build();
        }
    }
}
