package com.techelevator.tenmo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterUserDTOTest {

    @Test
    public void test_register_getAndSetUsername() {
        String name = "Clifford";
        RegisterUserDTO register1 = new RegisterUserDTO();
        register1.setUsername(name);

        assertEquals(name, register1.getUsername());

        String name2 = "Joe Smoe";
        RegisterUserDTO register2 = new RegisterUserDTO();
        register2.setUsername(name2);

        assertEquals(name2, register2.getUsername());

    }


    @Test
    public void test_register_getAndSetPassword() {
        String password = "dog123";
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setPassword(password);

        assertEquals(password, registerUserDTO.getPassword());

        String password2 = "123cool";
        RegisterUserDTO registerUserDTO2 = new RegisterUserDTO();
        registerUserDTO2.setPassword(password2);

        assertEquals(password2, registerUserDTO2.getPassword());
    }


    @Test
    public void setPassword() {
    }
}