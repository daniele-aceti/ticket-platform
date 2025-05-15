package ticket.platform.ticket_platform.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ticket.platform.ticket_platform.model.Role;
import ticket.platform.ticket_platform.model.User;

public class DatabaseUserDetails implements UserDetails {

    private final Long id;
    private final String name;
    private final String password;
    private final Boolean active;
    private final List<GrantedAuthority> authorities;
    private final String email;

    public DatabaseUserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.authorities = new ArrayList<>();
        for (Role ruolo : user.getRole()) {
            this.authorities.add(new SimpleGrantedAuthority(ruolo.getRoleName()));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public Boolean getActive() {
        return active;
    }
}
