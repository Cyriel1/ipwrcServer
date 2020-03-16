package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.service.JsonViewService;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public class User implements Principal {

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private long userID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String username;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String password;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String role;

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

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }
}
