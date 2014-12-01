package com.vssnake.potlach.server.repository;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MemoryTokenRepository implements TokenRepository {

	private static MemoryTokenRepository singleton;
	public static MemoryTokenRepository getInstance(){
		if (singleton == null){
			singleton = new MemoryTokenRepository();
		}
		return singleton;
	}
	
	HashMap<String,String> hashMapCredentials = new LinkedHashMap<String, String>();
	HashMap<String,String> hashMapUsers = new LinkedHashMap<String, String>();
	@Override
	public String getToken(String userID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean checkCredentials(String userID, String token) {
		// TODO Auto-generated method stub
		String realID = hashMapCredentials.get(token);
		if (userID.equals(realID)){
			return true;
		}
		return false;
	}

	@Override
	public String saveCredentials(String email, String token) {
		// TODO Auto-generated method stub
		if ( hashMapCredentials.put(token, email) != null){
			return hashMapUsers.put(email, null);
		}
		return null;
	}


	@Override
	public String getEmail(String tokenID) {
		// TODO Auto-generated method stub
		return hashMapCredentials.get(tokenID);
	}


	@Override
	public boolean removeUser(String tokenID, String email) {
		hashMapCredentials.remove(tokenID);
		hashMapUsers.remove(email);
		return true;
	}

}
