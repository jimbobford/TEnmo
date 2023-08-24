package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Transfer {

    
    private int transferId;
    @Min(value = 1, message = "The currentBid field must be greater than 0.")
    private BigDecimal transferAmount;
    private int from;
    private int to;
    private String usernameFrom;
    private String usernameTo;


    public Transfer(int transferId, BigDecimal transferAmount, int from, int to) {
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

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferAmount=" + transferAmount +
                ", from='" + usernameFrom + '\'' +
                ", to='" + usernameTo + '\'' +
                '}';
    }
}
