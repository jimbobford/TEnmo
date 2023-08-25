package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AuthorityTest {

    @Test
    public void test_getAndSetName() {
        String name = "user";
        Authority account1 = new Authority(name);

        assertEquals(name, account1.getName());

        String name2 = "admin";
        Authority account2 = new Authority(name2);

        assertEquals(name2, account2.getName());
    }

}