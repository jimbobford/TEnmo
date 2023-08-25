package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int transferId);

    Transfer createTransfer(Transfer transfer);

    List<Transfer> userTransferList (String username);

    Transfer userTransferById(String name, int id);
}
