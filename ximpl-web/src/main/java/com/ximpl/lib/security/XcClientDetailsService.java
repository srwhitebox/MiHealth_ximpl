package com.ximpl.lib.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class XcClientDetailsService  implements ClientDetailsService{
	private String id;
    private String secretKey;
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getSecretKey() {
        return secretKey;
    }
 
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

	public ClientDetails loadClientByClientId(String arg0) throws ClientRegistrationException {
		
		List<String> authorizedGrantTypes = new ArrayList<String>();
        authorizedGrantTypes.add("password");
        authorizedGrantTypes.add("refresh_token");
        authorizedGrantTypes.add("client_credentials");
        authorizedGrantTypes.add("authorization_code");
        authorizedGrantTypes.add("implicit");
 
        XcClientDetails clientDetails = null;		// Find Client details from the DB
        
//        clientDetails.setClientId(id);
//        clientDetails.setClientSecret(secretKey);

//        clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
 
        return clientDetails;
	}
}
