package com.techelevator.tenmo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginDTOTest {

    @Test
    public void test_login_getAndSetUsername() {
        String name = "Clifford";
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(name);

        assertEquals(name, loginDTO.getUsername());

        String name2 = "Joe Smoe";
        LoginDTO loginDTO2 = new LoginDTO();
        loginDTO2.setUsername(name2);

        assertEquals(name2, loginDTO2.getUsername());
    }
    @Test
    public void test_login_getAndSetPassword() {
        String password = "dog123";
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword(password);

        assertEquals(password, loginDTO.getPassword());

        String password2 = "123cool";
        LoginDTO loginDTO2 = new LoginDTO();
        loginDTO2.setPassword(password2);

        assertEquals(password2, loginDTO2.getPassword());
    }

}