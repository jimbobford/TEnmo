package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<Username> findAllUsers();

    User findByUsername(String username);

    int findIdByUsername(String username);

    String findUsernameById(int id);

    boolean create(String username, String password);

    Account retrieveBalance(String username);

    Transfer getTransferById(int transferId);

    Transfer createTransfer(Transfer transfer);

    List<Transfer> userTransferList (String username);

    Transfer userTransferById(String name, int id);
}
