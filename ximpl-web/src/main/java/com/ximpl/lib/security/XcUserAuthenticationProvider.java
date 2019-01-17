package com.ximpl.lib.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class XcUserAuthenticationProvider implements AuthenticationProvider{

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		boolean result = false; // aaaProxy.isValidUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
		 
        if (result) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            XcUserAuthenticationToken auth = new XcUserAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorities);
 
            return auth;
        } else {
            throw new BadCredentialsException("Bad User Credentials.");
        }

	}

	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
