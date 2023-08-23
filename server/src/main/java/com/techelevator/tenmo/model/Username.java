package com.techelevator.tenmo.model;

public class Username {

    private String username;

    public Username (String username){
        this.username = username;
    }

    public Username(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "username : " + username;
    }
}
