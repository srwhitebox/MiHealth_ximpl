package com.ximpl.lib.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;

public class UserCredential extends ClientCredentialsTokenEndpointFilter {
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		if (false){ //SECProxy.isCookieValid(request)) {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("myUser", "mycompanykey");
 
            // Authenticate the user
            Authentication auth = this.getAuthenticationManager().authenticate(authRequest);
 
            return auth;
        } else {
    		// No user Authenticated
        	Authentication auth = this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken("invalidUser", "invalidCompanyName"));
            auth.setAuthenticated(false);
 
            return auth;
        }		
	}
    
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}
}
