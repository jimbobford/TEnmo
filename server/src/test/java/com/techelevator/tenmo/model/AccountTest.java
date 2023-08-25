package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void test_getAndSetUsername() {
        Account account = new Account();
        account.setUsername("bob");

        assertEquals("bob", account.getUsername());

        Account account1 = new Account();
        account1.setUsername("Joe");

        assertEquals("Joe", account1.getUsername() );
    }


    @Test
    public void test_getAndSetBalance() {
        BigDecimal balance1 = new BigDecimal(1000);
        Account account1 = new Account();
        account1.setBalance(balance1);

        assertEquals(balance1, account1.getBalance());

        BigDecimal balance2 = new BigDecimal(30000);
        Account account2 = new Account();
        account2.setBalance(balance2);

        assertEquals(balance2, account2.getBalance());
    }


}