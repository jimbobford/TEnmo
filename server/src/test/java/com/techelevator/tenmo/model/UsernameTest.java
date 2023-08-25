package com.techelevator.tenmo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UsernameTest {

    @Test
    public void test_username_getAndSetUsername() {
        String name = "Larry";
        Username username1 = new Username();
        username1.setUsername(name);

        assertEquals(name, username1.getUsername());

        String name2 = "Sammy";
        Username username2 = new Username();
        username2.setUsername(name2);

        assertEquals(name2, username2.getUsername());
    }
}