package com.vssnake.potlach.server.repository;

public interface TokenRepository {

	public String getToken(String userID);
	

	
	public boolean checkCredentials(String userID,String token);
	
	public String saveCredentials(String email,String token);
	
	public String getEmail(String tokenID);
	
	public boolean removeUser(String tokenID,String email);
}
