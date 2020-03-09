package nl.ipwrcServer.model.builder;

import nl.ipwrcServer.model.User;

public class UserBuilder {

    private long userID;
    private String username;
    private String password;
    private String role;

    public UserBuilder setUserID(long userID){
        this.userID = userID;

        return this;
    }

    public UserBuilder setUsername(String username){
        this.username = username;

        return this;
    }

    public UserBuilder setPassword(String password){
        this.password = password;

        return this;
    }

    public UserBuilder setRole(String role){
        this.role = role;

        return this;
    }

    public long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public User build(){
        return new User(this);
    }
}
