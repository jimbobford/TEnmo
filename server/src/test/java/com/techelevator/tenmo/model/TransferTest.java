package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferTest {

    @Test
    public void test_getAndSetTransferId() {
        int id1 = 3003;
        Transfer transfer1 = new Transfer();
        transfer1.setTransferId(id1);

        assertEquals(id1, transfer1.getTransferId());

        int id2 = 3203;
        Transfer transfer2 = new Transfer();
        transfer2.setTransferId(id2);

        assertEquals(id2, transfer2.getTransferId());

    }


    @Test
    public void test_getAndSetTransferAmount() {

        BigDecimal amount1 = new BigDecimal(50);
        Transfer transfer1 = new Transfer();
        transfer1.setTransferAmount(amount1);

        assertEquals(amount1, transfer1.getTransferAmount());

        BigDecimal amount2 = new BigDecimal(300);
        Transfer transfer2 = new Transfer();
        transfer2.setTransferAmount(amount2);

        assertEquals(amount2, transfer2.getTransferAmount());
    }

    @Test
    public void test_getAndSetFrom() {
        int id1 = 1003;
        Transfer transfer1 = new Transfer();
        transfer1.setFrom(id1);

        assertEquals(id1, transfer1.getFrom());

        int id2 = 1203;
        Transfer transfer2 = new Transfer();
        transfer2.setFrom(id2);

        assertEquals(id2, transfer2.getFrom());
    }


    @Test
    public void test_getAndSetTo() {
        int id1 = 1008;
        Transfer transfer1 = new Transfer();
        transfer1.setTo(id1);

        assertEquals(id1, transfer1.getTo());

        int id2 = 1232;
        Transfer transfer2 = new Transfer();
        transfer2.setTo(id2);

        assertEquals(id2, transfer2.getTo());
    }


    @Test
    public void test_getAndSetUsernameFrom() {
        String name = "Charles";
        Transfer transfer1 = new Transfer();
        transfer1.setUsernameFrom(name);

        assertEquals(name, transfer1.getUsernameFrom());

        String name2 = "Patrick";
        Transfer transfer2 = new Transfer();
        transfer2.setUsernameFrom(name2);

        assertEquals(name2, transfer2.getUsernameFrom());
    }


    @Test
    public void test_getAndSetUsernameTo() {
        String name = "Sally";
        Transfer transfer1 = new Transfer();
        transfer1.setUsernameTo(name);

        assertEquals(name, transfer1.getUsernameTo());

        String name2 = "Frank";
        Transfer transfer2 = new Transfer();
        transfer2.setUsernameTo(name2);

        assertEquals(name2, transfer2.getUsernameTo());
    }
}