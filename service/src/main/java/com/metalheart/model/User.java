package com.metalheart.model;

import com.metalheart.log.LogContextField;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Arrays.asList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User implements UserDetails, Cloneable {

    @LogContextField(LogContextField.Field.USER_ID)
    private Integer id;

    private String username;

    private String password;

    @LogContextField(LogContextField.Field.EMAIL)
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

    @Override
    public User clone() {
        return toBuilder().build();
    }
}
