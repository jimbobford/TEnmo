package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account getAccountByUserId(int id){

        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        Account account = null;

        if(results.next()){
            account = mapRowToAccount(results);
        }
        return account;
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

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
