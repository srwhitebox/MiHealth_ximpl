package com.ximpl.lib.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

@SuppressWarnings("serial")
public class XcClientDetails implements ClientDetails{

	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientSecret() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAutoApprove(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isScoped() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return false;
	}
}
