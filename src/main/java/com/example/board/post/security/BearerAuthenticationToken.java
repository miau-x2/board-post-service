package com.example.board.post.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class BearerAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private @Nullable Object credentials;

    public BearerAuthenticationToken(Object principal, @Nullable Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static BearerAuthenticationToken authenticated(Object principal, @Nullable Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new BearerAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public @Nullable Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
