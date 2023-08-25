package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int transferId);

    Transfer createTransfer(String name, Transfer transfer);

    List<Transfer> userTransferList (String username);

    Transfer userTransferById(String name, int id);

    Transfer requestTransfer(String name, Transfer transfer);

    Transfer updateStatus(String name, Transfer transfer);
}
