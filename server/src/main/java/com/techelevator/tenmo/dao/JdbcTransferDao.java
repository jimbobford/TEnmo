package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Username;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final String TRANSFER_SELECT = "SELECT transfer_id, transfer_amount, from_user_id, to_user_id, date, status FROM transfer ";

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = TRANSFER_SELECT+
                "WHERE transfer_id=?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Unable to connect to server or database", e);
            System.out.println("Unable to connect to server or database");
        }
        return transfer;
    }

    public Transfer createTransfer(String name, Transfer transfer) {
        Transfer newTransfer = null;
        int newTransferId = 0;
        String sql = "INSERT INTO transfer (transfer_amount, from_user_id, to_user_id, status) " +
                "VALUES (?, (SELECT user_id FROM tenmo_user WHERE username = ?), (SELECT user_id FROM tenmo_user WHERE username = ?), ?) " +
                "RETURNING transfer_id";
        if(transfer.getUsernameFrom().equals(transfer.getUsernameTo())) {
            throw new DataIntegrityViolationException("Please select a new person to receive money.");
        }

        if(!transfer.getUsernameFrom().equals(name)) {
            throw new DataIntegrityViolationException("You can't send money from another user to yourself.");
        }

        String sql1 = "SELECT balance FROM account \n" +
                "WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?);";

        SqlRowSet balanceResult = jdbcTemplate.queryForRowSet(sql1, transfer.getUsernameFrom());
        BigDecimal currentBalance = new BigDecimal("0");

        if(balanceResult.next()) {
            currentBalance = balanceResult.getBigDecimal("balance");
        }

        BigDecimal trialBalance = currentBalance.subtract(transfer.getTransferAmount());
        BigDecimal zeroBd = new BigDecimal("0");

        if(trialBalance.compareTo(zeroBd) == -1) {
            throw new DataIntegrityViolationException("Insufficient funds.");
        }

        if(transfer.getTransferAmount().compareTo(BigDecimal.ZERO)== 0) {
            throw new DataIntegrityViolationException("Can't send $0.00.");
        }

        if(transfer.getTransferAmount().compareTo(BigDecimal.ZERO)== -1) {
            throw new DataIntegrityViolationException("Can't send negative amount.");
        }

        try {
            newTransferId = jdbcTemplate.queryForObject(sql,int.class, transfer.getTransferAmount(), transfer.getUsernameFrom(),
                    transfer.getUsernameTo(), 1);

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println ("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation");
        } catch (NullPointerException e){
            System.out.println("There's a problem");
        }

        transferUpdate(newTransferId, newTransfer, transfer);

        return newTransfer;
    }


    public Transfer transferUpdate(int newTransferId, Transfer newTransfer, Transfer transfer){
        String sql1 = "SELECT balance FROM account \n" +
                "WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?);";

        SqlRowSet balanceResult = jdbcTemplate.queryForRowSet(sql1, transfer.getUsernameFrom());
        BigDecimal currentBalance = new BigDecimal("0");

        if(balanceResult.next()) {
            currentBalance = balanceResult.getBigDecimal("balance");
        }

        BigDecimal trialBalance = currentBalance.subtract(transfer.getTransferAmount());
        BigDecimal zeroBd = new BigDecimal("0");

        if(trialBalance.compareTo(zeroBd) == -1) {
            throw new DataIntegrityViolationException("Insufficient funds.");
        }

        String sqlFrom = "UPDATE account\n" +
                "SET balance = balance - ?\n" +
                "WHERE account_id =\n" +
                "(SELECT account_id FROM account \n" +
                " JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                " WHERE username = ?);";
        String sqlTo = "UPDATE account\n" +
                "SET balance = balance + ?\n" +
                "WHERE account_id =\n" +
                "(SELECT account_id FROM account \n" +
                " JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                " WHERE username = ?);";
        try{
            int rowsAffected = jdbcTemplate.update(sqlFrom, transfer.getTransferAmount(), transfer.getUsernameFrom());
            int rowsAffected2 = jdbcTemplate.update(sqlTo,transfer.getTransferAmount(), transfer.getUsernameTo());
            if(rowsAffected + rowsAffected2 < 2){
//                throw new DaoException("Zero rows affected, expected at least one");
                System.out.println("Zero or one row affected, expected at least two");
            } else {
                newTransfer = getTransferById(newTransferId);
            }
        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Unable to connect to server or database", e);
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e){
//            throw new DaoException("Data integrity violation", e);
            System.out.println("Unable to connect to server or database");
        } catch (NullPointerException e){
            System.out.println("There's a problem updating the accounts");
        }
        return newTransfer;
    }

    public Transfer requestTransfer(String name, Transfer transfer) {
        Transfer newTransfer = null;
        int newTransferId = 0;
        String sql = "INSERT INTO transfer (transfer_amount, from_user_id, to_user_id, status) " +
                "VALUES (?, (SELECT user_id FROM tenmo_user WHERE username = ?), (SELECT user_id FROM tenmo_user WHERE username = ?), ?) " +
                "RETURNING transfer_id";
        if(transfer.getUsernameFrom().equals(transfer.getUsernameTo())) {
            throw new DataIntegrityViolationException("Please select a new person to request money from.");
        }

        if(!transfer.getUsernameTo().equals(name)) {
            throw new DataIntegrityViolationException("You can't send money to another user.");
        }

        if(transfer.getTransferAmount().compareTo(BigDecimal.ZERO)== 0) {
            throw new DataIntegrityViolationException("Can't send $0.00.");
        }

        if(transfer.getTransferAmount().compareTo(BigDecimal.ZERO)== -1) {
            throw new DataIntegrityViolationException("Can't send negative amount.");
        }

        try {
            newTransferId = jdbcTemplate.queryForObject(sql,int.class, transfer.getTransferAmount(), transfer.getUsernameFrom(),
                    transfer.getUsernameTo(), 2);
            newTransfer = getTransferById(newTransferId);
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println ("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation");
        } catch (NullPointerException e){
            System.out.println("There's a problem");
        }

        return newTransfer;
    }


    public Transfer updateStatus(String name, Transfer transfer) {
        Transfer updatedTransfer = null;

        String sql = "UPDATE transfer SET status = ? WHERE transfer_id = ? AND to_user_id = " +
                "(SELECT user_id FROM tenmo_user WHERE username = ?);";
        try {
            int numberOfRows = jdbcTemplate.update(sql, transfer.getTransferId(), name);

            if (numberOfRows == 0) {
                throw new DataIntegrityViolationException("Zero rows affected, expected at least one");
            } else {
                updatedTransfer = getTransferById(transfer.getTransferId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DataIntegrityViolationException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Data integrity violation", e);
        }

        String sql1 = "SELECT status FROM transfer \n" +
                "WHERE transfer_id = ?;";
        SqlRowSet statusResult = jdbcTemplate.queryForRowSet(sql1, transfer.getTransferId());

        int status = 0;
        if(statusResult.next()) {
           status = updatedTransfer.getStatus();
        }
        if(status == 1) {
            transferUpdate(transfer.getTransferId(), updatedTransfer, transfer);
        }

        return updatedTransfer;
    }

    public List<Transfer> userTransferList (String username){
        List <Transfer> activityList = new ArrayList<>();
        String sql = TRANSFER_SELECT +
                "WHERE from_user_id = (SELECT user_id FROM tenmo_user WHERE username = ?) " +
                "OR to_user_id = (SELECT user_id FROM tenmo_user WHERE username = ?);";


        // result does not have usernames yet

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username );
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            activityList.add(transfer);

            setUsername(transfer);

        }
        return activityList;

    }

    public Transfer userTransferById(String name, int id){
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_amount, from_user_id, to_user_id " +
                "FROM transfer " +
                "JOIN tenmo_user ON tenmo_user.user_id = transfer.from_user_id " +
                "WHERE (from_user_id = (SELECT user_id FROM tenmo_user WHERE username = ?) " +
                "OR to_user_id = (SELECT user_id FROM tenmo_user WHERE username = ?)) AND transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name, name, id);
        if(results.next()){
            transfer = mapRowToTransfer(results);
            setUsername(transfer);
        }

        if(transfer == null) {
            throw new DataIntegrityViolationException("This id is not one of your transactions.");
        }
        return transfer;

    }

    public void setUsername (Transfer transfer){
        String sqlFrom = "SELECT username FROM tenmo_user WHERE user_id= ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFrom, transfer.getFrom());
        if(result.next()){
            Username usernameFrom = mapRowToUsername(result);
            String name = String.valueOf(usernameFrom);
            String usernameString = name.substring(name.indexOf("'")+1, name.length()-2);
            transfer.setUsernameFrom(usernameString);
        }

        String sqlTo = "SELECT username FROM tenmo_user WHERE user_id= ?";

        SqlRowSet resultTo = jdbcTemplate.queryForRowSet(sqlTo, transfer.getTo());
        if(resultTo.next()){
            Username usernameTo = mapRowToUsername(resultTo);
            String name = String.valueOf(usernameTo);
            String usernameString = name.substring(name.indexOf("'")+1, name.length()-2);
            transfer.setUsernameTo(String.valueOf(usernameString));
        }
    }


    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transfer.setFrom(rs.getInt("from_user_id"));
        transfer.setTo(rs.getInt("to_user_id"));
        return transfer;
    }
    private Username mapRowToUsername(SqlRowSet rs) {
        Username username = new Username();
        username.setUsername(rs.getString("username"));
        return username;
    }
}

