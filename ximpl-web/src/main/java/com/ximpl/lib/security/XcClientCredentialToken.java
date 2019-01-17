package com.ximpl.lib.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;

public class XcClientCredentialToken extends ClientCredentialsTokenEndpointFilter{
	
	private XcClientDetailsService clientDetails;
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		
        if (clientDetails.getId().equals(request.getParameter("client_id"))) {
 
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientDetails.getId(), "mycompanykey");
 
            // Authenticate the user
            Authentication auth = this.getAuthenticationManager().authenticate(authRequest);
 
            return auth;
        } else {
            // No user Authenticated
            Authentication auth = this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken("invalidUser",
                            "invalidCompanyName"));
            auth.setAuthenticated(false);
 
            return auth;
        }
 
    }
 
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
 
        return true;
    }
 
    public XcClientDetailsService getClientDetails() {
        return clientDetails;
    }
 
    public void setClientDetails(XcClientDetailsService clientDetails) {
        this.clientDetails = clientDetails;
    }
    
}
