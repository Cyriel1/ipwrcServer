package nl.ipwrcServer.model;

import com.fasterxml.jackson.annotation.JsonView;
import nl.ipwrcServer.service.JsonViewService;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;

public class Role implements Principal {

    @NotEmpty
    @JsonView(JsonViewService.Protected.class)
    private long roleID;

    @NotEmpty
    @JsonView(JsonViewService.Public.class)
    private String role;

    private User user;

    public Role(long roleID, String role){
        this.roleID = roleID;
        this.role = role;
    }

    public Role(String role){
        this.role = role;
    }

    public long getRoleID() {
        return roleID;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getName() {
        return user.getName();
    }
}
