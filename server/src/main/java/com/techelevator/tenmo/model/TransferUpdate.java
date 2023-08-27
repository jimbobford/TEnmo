package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class TransferUpdate {
    private int transferId;
    private int status;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
