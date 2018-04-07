package org.java.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInInterceptor;
import org.java.rest.controller.AccountController;
import org.java.rest.controller.impl.AccountControllerImpl;
import org.java.rest.mapper.ConstraintViolationExceptionMapper;
import org.java.rest.service.TransferService;
import org.java.rest.service.impl.TransferServiceImpl;
import org.java.rest.store.AccountStore;

import java.util.Collections;

public class Server {

    private static final String DEFAULT_ENDPOINT = "http://0.0.0.0:9000";

    public static void main(String[] args) {
        String address = args.length > 0 ? args[0] : DEFAULT_ENDPOINT;

        AccountStore accountStore = new AccountStore();
        TransferService transferService = new TransferServiceImpl(accountStore);
        AccountController accountController = new AccountControllerImpl(transferService);

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setAddress(address);
        sf.setResourceClasses(AccountController.class);
        sf.setResourceProvider(AccountController.class, new SingletonResourceProvider(accountController));
        sf.setInInterceptors(Collections.singletonList(new JAXRSBeanValidationInInterceptor()));
        sf.setProvider(new JacksonJaxbJsonProvider());
        sf.setProvider(new ConstraintViolationExceptionMapper());
        sf.create();

        Runtime.getRuntime().addShutdownHook(new Thread(sf.getServer()::stop));
    }
}