package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public Account getBalance(Principal principal) {
        return userDao.retrieveBalance(principal.getName());
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<Username> usersList (){
        return userDao.findAllUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path= "/transfer", method = RequestMethod.POST)
    public Transfer transferMoney (@RequestBody Transfer transfer){
        return userDao.createTransfer(transfer);
    }







}
