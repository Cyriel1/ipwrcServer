package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.service.JsonViewService;
import nl.ipwrcServer.enums.Roles;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public class User implements Principal {

    @NotEmpty
    @JsonView(JsonViewService.Protected.class)
    private long userID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String username;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String password;

    @NotEmpty
    @JsonView(JsonViewService.Protected.class)
    private String role;

    @JsonIgnore
    private Roles roles;

    public User(Long userID, String username, String password, String role){
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(String role){
        this.role = role;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }

    @JsonIgnore
    public Roles getRoles() {
        return roles;
    }


    public long getUserID() {
        return userID;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole(){
        return role;
    }

}
