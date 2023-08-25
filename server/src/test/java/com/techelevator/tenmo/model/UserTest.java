package com.techelevator.tenmo.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    User user1 = new User(1001, "Bob", "bob123", "user");
    User user2 = new User(1002, "Mister Rogers", "bemyneighbor", "user, creator");

    @Test
    public void test_getId() {

        assertEquals(1001, user1.getId());


        assertEquals(1002, user2.getId());


    }
    @Test
    public void test_setId(){
        user1.setId(1202);
        assertEquals(1202, user1.getId());

        user2.setId(1206);
        assertEquals(1206, user1.getId());
    }



    @Test
    public void test_getUsername() {


        assertEquals("Bob", user1.getUsername());


        assertEquals("Mister Rogers", user2.getUsername());
    }

    @Test
    public void test_setUsername() {

        user1.setUsername("Joe");

        assertEquals("Joe", user1.getUsername());

        user2.setUsername("Taylor");

        assertEquals("Taylor", user2.getUsername());
    }

    @Test
    public void test_getPassword() {

        assertEquals("bob123", user1.getPassword());


        assertEquals("bemyneighbor", user2.getPassword());
    }
    @Test
    public void test_setPassword() {

        user1.setPassword("dog123");

        assertEquals("dog123", user1.getPassword());

        user2.setPassword("123cool");

        assertEquals("123cool", user2.getPassword());
    }


    @Test
    public void test_set_isActivated() {
        user1.isActivated();

        assertTrue(user1.isActivated());

        User user3 = new User();
        user3.isActivated();

        assertFalse(user3.isActivated());

    }

    @Test
    public void test_getAndSetAuthorities() {

        User user = new User(1001, "Bob", "bob123", "admin");

        Assert.assertEquals(user.getAuthorities(), user1.getAuthorities());


    }



}