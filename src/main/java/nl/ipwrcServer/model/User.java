package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.model.builder.UserBuilder;
import nl.ipwrcServer.service.JsonViewService;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;

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

    public User(UserBuilder userBuilder){
        this.userID = userBuilder.getUserID();
        this.username = userBuilder.getUsername();
        this.password = userBuilder.getPassword();
        this.role = userBuilder.getRole();
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

    @Override
    @JsonIgnore
    public String getName() {
        return username;
    }

}
