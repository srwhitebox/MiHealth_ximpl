package com.ximpl.lib.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UserRole {
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER = "ROLE_USER";
	public static final String NURSE = "ROLE_NURSE";
	public static final String STUDENT = "ROLE_STUDENT";
	public static final String TEACHER = "ROLE_TEACHER";
	public static final String PARENT = "ROLE_PARENT";
	public static final String SUPERVISOR = "ROLE_SUPERVISOR";
	
	public static List<GrantedAuthority> getAuthorities(Set<String> roles){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String role : roles){
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	public static List<GrantedAuthority> getAuthorities(String... roles){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String role : roles){
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	public static List<GrantedAuthority> getAuthorities(JsonObject roles){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Entry<String, JsonElement> entry : roles.entrySet()){
			authorities.add(new SimpleGrantedAuthority(entry.getKey()));
		}
		return authorities;
	}

	public static List<GrantedAuthority> getAuthorities(XcMap roles){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String key : roles.keySet()){
			if (roles.getBoolean(key)){
				authorities.add(new SimpleGrantedAuthority(key));
			}
		}
		return authorities;
	}

}
