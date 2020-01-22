package com.metalheart.security;

import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class Token extends AbstractAuthenticationToken {

    private UserDetails principal;
    private Object credentials;

    public Token(UserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }
}