package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public Account getBalance(Principal principal) {
        return userDao.retrieveBalance(principal.getName());
    }

}
