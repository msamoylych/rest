package org.java.rest.store.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Account {

    private String id;

    private BigDecimal amount;
}
