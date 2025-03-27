package org.unibl.etf.model.users;

import org.unibl.etf.util.TextHasher;


public abstract class User {
    private static int idCounter = 0;

    private int id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private boolean isActive;

    public User() {}

    public User(int id, String username, String password, String firstName, String lastName,
                String email, String userType, boolean isActive) {
        this.id = id;
        this.username = username;
        if(password.length() == 64){    // SHA-256 hex length
            this.passwordHash = password;
        }
        else{
            this.passwordHash = TextHasher.getHash(password);
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userType = userType;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
