package com.metalheart.model;

import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Arrays.asList;

@Data
public class User implements UserDetails {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private ZonedDateTime lastLoginAt;

    private ZonedDateTime createdAt;

    private ZonedDateTime modifiedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return asList(new SimpleGrantedAuthority("USER_ROLE"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
