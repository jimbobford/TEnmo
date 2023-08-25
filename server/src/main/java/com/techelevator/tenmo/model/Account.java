package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private String username;

    private BigDecimal balance;


    public Account(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
