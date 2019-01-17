package com.ximpl.lib.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class XcUserAuthenticationToken extends AbstractAuthenticationToken{
	private static final long serialVersionUID = 503883598783793420L;
	private final Object principal;
    private Object credentials;
    
	public XcUserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
	}

	public Object getCredentials() {
		return this.credentials;
	}

	public Object getPrincipal() {
		return this.principal;
	}
}
