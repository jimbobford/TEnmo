package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Transfer {

    
    private int transferId;
    private Account account;
    @Min(value = 1, message = "The currentBid field must be greater than 0.")
    private BigDecimal transferAmount;
    private String from;
    private String to;


    public Transfer(int transferId, BigDecimal transferAmount, String from, String to) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.from = from;
        this.to = to;
    }

    public Transfer() {

    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferAmount=" + transferAmount +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
