package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.validation.OneOf;
import nl.ipwrcServer.service.JsonViewService;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public class Account implements Principal {

    @NotEmpty
    @JsonIgnore
    private long accountID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String username;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String password;

    @NotEmpty
    @OneOf(value = {"ADMIN", "KLANT"}, ignoreCase = true, ignoreWhitespace = true)
    @JsonIgnore
    private String role;

    @JsonIgnore
    private String[] roles;

    public Account(){

    }

    public Account(String username, String[] roles){
        this.username = username;
        this.roles = roles;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getRole() {
        return role;
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
