package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

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
    public Transfer transferMoney (Principal principal, @RequestBody Transfer transfer){
        return transferDao.createTransfer(principal.getName(), transfer);
    }


    @RequestMapping(path="/activity", method = RequestMethod.GET)
    public List<Transfer> activityList (Principal principal){
        return transferDao.userTransferList(principal.getName());
    }

    @RequestMapping(path="/activity/{id}", method = RequestMethod.GET)
    public Transfer activityList (Principal principal, @PathVariable int id){
        return transferDao.userTransferById(principal.getName(), id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/request", method = RequestMethod.POST)
    public Transfer requestMoney(Principal principal, @RequestBody Transfer transfer) {
        return transferDao.requestTransfer(principal.getName(), transfer);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path="/request", method = RequestMethod.PUT)
    public Transfer requestPending(Principal principal, @RequestBody Transfer transfer) {
        return transferDao.updateStatus(principal.getName(), transfer);
    }


}
