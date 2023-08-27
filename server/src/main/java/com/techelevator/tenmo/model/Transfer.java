package com.techelevator.tenmo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Transfer {

    
    private int transferId;
    private LocalDate date;
    @Min(value = 1, message = "The field must be greater than 0.")
    private BigDecimal transferAmount;
    @JsonIgnore
    private int from;
    private String usernameFrom;
    @JsonIgnore
    private int to;
    private String usernameTo;
//    private String status;
    private int statusId;


    public Transfer(int transferId, BigDecimal transferAmount, int from, int to) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.from = from;
        this.to = to;
        this.usernameFrom = getUsernameFrom();
        this.usernameTo = getUsernameTo();
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

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
