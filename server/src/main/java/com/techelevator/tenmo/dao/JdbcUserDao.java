package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {
    private final String TRANSFER_SELECT = "SELECT transfer_id, transfer_amount, from_user_id, to_user_id, date, status FROM transfer ";
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public String findUsernameById(int id) {
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ?;";
        String username = jdbcTemplate.queryForObject(sql,String.class,id);
        if(username != null) {
            return username;
        } else {
            return null;
        }

    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<Username> findAllUsers() {
        List<Username> usernames = new ArrayList<>();
        String sql = "SELECT username FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Username username = mapRowToUsername(results);
            usernames.add(username);
        }
        return usernames;
    }


    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // TODO: Create the account record with initial balance
        String accountSql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";

        int rowsInserted = jdbcTemplate.update(accountSql, newUserId, 1000);

        return true;
    }

    @Override
    public Account retrieveBalance(String username) {

        String sql = "SELECT username, balance " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        Account account = null;

        if(results.next()) {
            account = new Account();
            account.setUsername(results.getString("username"));
            account.setBalance(results.getBigDecimal("balance"));
        }

        return account;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }

    private Username mapRowToUsername(SqlRowSet rs) {
        Username username = new Username();
        username.setUsername(rs.getString("username"));
        return username;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transfer.setFrom(rs.getInt("from_user_id"));
        transfer.setTo(rs.getInt("to_user_id"));
        return transfer;
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

    public Transfer createTransfer(Transfer transfer) {
        Account account = null;
        Transfer newTransfer = null;
        int newTransferId = 0;
        String sql = "INSERT INTO transfer (transfer_amount, from_user_id, to_user_id, status) " +
                "VALUES (?, (SELECT user_id FROM tenmo_user WHERE username = ?), (SELECT user_id FROM tenmo_user WHERE username = ?), ?) " +
                "RETURNING transfer_id";
        if(transfer.getUsernameFrom().equals(transfer.getUsernameTo())) {
            throw new DataIntegrityViolationException("Please select a new person to receive money.");
        }

//        if(transfer.getTransferAmount().compareTo(account.getBalance())==1) {
//            throw new DataIntegrityViolationException("Insufficient funds.");
//        }

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
            System.out.println("There's a problem");
        }
        return newTransfer;
    }
    //6
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

}
