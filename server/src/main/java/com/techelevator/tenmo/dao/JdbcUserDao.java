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
    private final String TRANSFER_SELECT = "SELECT transfer_id, transfer_amount, from_username, to_username FROM transfer" +
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


    @Override
    public boolean createTransfer(BigDecimal amount, String from, String to) {

        String sql = "INSERT INTO transfer (transfer_amount, from_username, to_username) VALUES (?, ?, ?) RETURNING transfer_id";
        Integer newTransferId;
        try {
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, amount, from, to);
        } catch (DataAccessException e) {
            return false;
        }

        String accountSql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";

        int rowsInserted = jdbcTemplate.update(accountSql, newTransferId, 1000);

        return true;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_amount, from_username, to_username FROM transfer"+
                " WHERE e.employee_id=?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                employee = mapRowToEmployee(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return employee;
    }

    @Override
    public Transfer createTransfer(Transfer transfer, Account account) {
        Transfer newTransfer = null;
        int newTransferId = 0;
        String sql = "INSERT INTO transfer (transfer_amount, from_username, to_username) VALUES (?, ?, ?) RETURNING transfer_id";
        try{
            newTransferId = jdbcTemplate.queryForObject(sql,int.class, transfer.getTransferAmount(), transfer.getFrom(),
                    transfer.getTo());

            if(transfer.getFrom().equals(transfer.getTo())) {
                throw new DataIntegrityViolationException("Please select a new person to receive money.");
            }
            if(transfer.getTransferAmount().compareTo(account.getBalance())==1) {
                throw new DataIntegrityViolationException("Insufficient funds.");
            }
            if(transfer.getTransferAmount().compareTo(BigDecimal.ZERO)==-1) {
                throw new DataIntegrityViolationException("Can't send less than $0.00.");
            }
        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Unable to connect to server or database", e);
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e){
//            throw new DaoException("Data integrity violation", e);
            System.out.println("Unable to connect to server or database");
        }

        String sqlFrom = "UPDATE account\n" +
                "SET account_id = \n" +
                "(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?), \n" +
                "SET user_id =\n" +
                "(SELECT user_id FROM user WHERE username = ?), \n" +
                "SET balance = balance - ?;";
        try{
            int rowsAffected = jdbcTemplate.update(sqlFrom,transfer.getFrom(),transfer.getFrom(),transfer.getTransferAmount());
            if(rowsAffected == 0){
//                throw new DaoException("Zero rows affected, expected at least one");
                System.out.println("Zero rows affected, expected at least one");
//            } else {
////                newTransfer = getEmployeeById(employee.getId());
            }
        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Unable to connect to server or database", e);
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e){
//            throw new DaoException("Data integrity violation", e);
            System.out.println("Unable to connect to server or database");
        }

        String sqlTo = "UPDATE account\n" +
                "SET account_id = \n" +
                "(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?), \n" +
                "SET user_id =\n" +
                "(SELECT user_id FROM user WHERE username = ?), \n" +
                "SET balance = balance + ?;";

        try{
            int rowsAffected = jdbcTemplate.update(sqlTo,transfer.getTo(),transfer.getTo(),transfer.getTransferAmount());
            if(rowsAffected == 0){
//                throw new DaoException("Zero rows affected, expected at least one");
                System.out.println("Zero rows affected, expected at least one");
            } else {
                newTransfer = getTransferById(newTransferId);
            }
        } catch (CannotGetJdbcConnectionException e){
//            throw new DaoException("Unable to connect to server or database", e);
            System.out.println("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e){
//            throw new DaoException("Data integrity violation", e);
            System.out.println("Unable to connect to server or database");
        }

//		throw new DaoException("createEmployee() not implemented");
        return newTransfer;
    }
}
