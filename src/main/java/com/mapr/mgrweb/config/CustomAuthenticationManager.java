package com.mapr.mgrweb.config;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationManager.class);

    public CustomAuthenticationManager() {
        log.debug("CustomAuthenticationManager Initialized ...");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            log.debug("AUTHENTICATION Login" + authentication.getName());
            log.debug("AUTHENTICATION Password" + authentication.getCredentials().toString());

            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            boolean isAuthenticated = pamAuthentication(username, password);
            if (isAuthenticated) {
                List<GrantedAuthority> grantedAuths = new ArrayList<>();
                grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (Exception e) {
            throw new AuthenticationServiceException("Failed to login", e);
        }
    }

    public boolean pamAuthentication(String user, String passwd) throws AuthenticationException {
        return true;
    }
}
