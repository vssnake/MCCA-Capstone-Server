package com.vssnake.potlach.server.repository;

import java.util.Collection;

import com.vssnake.potlach.server.model.Gift;
import com.vssnake.potlach.server.model.GiftCreator;
import com.vssnake.potlach.server.model.SpecialInfo;
import com.vssnake.potlach.server.model.User;

public interface PotlachRepository {
	
	
	public User addUser(User user);
	
	public User getUser(String email);
	
	public User getUserWithToken(String token);
	
	
	public Collection<Gift> findByTitle(String title);
	
	public Collection<Gift> getLastGifts(String token,int startBy);
	
	public Gift getGift(Long id);
	
	public Collection<Gift> showUserGift(String email);
	
	public Collection<Gift> showGiftChain(Long idGift);
	
	public Gift createGift(GiftCreator giftCreator,Long idChain,String token);
	
	public boolean deleteGift(Long id,User user);
	
	public SpecialInfo showSpecialInfo();
}
