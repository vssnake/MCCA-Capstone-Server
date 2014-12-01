package com.vssnake.potlach.server.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vssnake.potlach.server.GCMBroadcast;
import com.vssnake.potlach.server.ServerApi;
import com.vssnake.potlach.server.model.Gift;
import com.vssnake.potlach.server.model.GiftCreator;
import com.vssnake.potlach.server.model.User;
import com.vssnake.potlach.server.repository.PotlachRepository;

@Controller
public class GiftSvc {

	@Autowired
	private PotlachRepository potlachRepository;
	
	@Autowired
	GCMBroadcast gcmBroadcast;
	

	
	@RequestMapping(value = ServerApi.GIFTS_SHOW)
	public @ResponseBody Collection<Gift> getGifts(
			@RequestHeader(value=ServerApi.BEARER_TOKEN)String token,
			@RequestHeader(value=ServerApi.HEADER_START)int startBy){
		
				return potlachRepository.getLastGifts(token,startBy);
		
	}
	@RequestMapping(value = ServerApi.GIFT_SHOW)
	public @ResponseBody Gift getGift(
			@RequestHeader(value=ServerApi.HEADER_GIFT_ID)Long idGift){
		
				return potlachRepository.getGift(idGift);
		
	}
	
	@RequestMapping(value = ServerApi.USER_GIFTS)
	public @ResponseBody Collection<Gift> getUserGifts(
			@RequestHeader(value=ServerApi.HEADER_EMAIL)String email){
		
				return potlachRepository.showUserGift(email);
		
	}
	
	@RequestMapping(value = ServerApi.GIFT_CHAIN)
	public @ResponseBody Collection<Gift> getGiftChain(
			@RequestHeader(value=ServerApi.HEADER_GIFT_ID)Long giftID){
		
				return potlachRepository.showGiftChain(giftID);
		
	}
	
	@RequestMapping(value = ServerApi.GIFT_LIKE)
	public @ResponseBody Gift modifyGiftLike(
			@RequestHeader(value=ServerApi.BEARER_TOKEN)String token,
			@RequestHeader(value=ServerApi.HEADER_GIFT_ID)Long giftID){

				Gift gift =potlachRepository.getGift(giftID);
				User user = potlachRepository.getUserWithToken(token);
				boolean increment = false;
				if (user != null && gift !=null){
					if(user.giftLikeExist(giftID)) {
		                increment = false;
		                user.removeLike(gift.getId());
		            }else {
		                increment = true;
		                user.addLike(gift.getId());
		            }
				}
				gift.incrementDecrementLike(increment);
				return gift;
		
	}
	
	@RequestMapping(value = ServerApi.GIFT_OBSCENE)
	public @ResponseBody Gift modifyObscene(
			@RequestHeader(value=ServerApi.HEADER_GIFT_ID)Long giftID){
			Gift gift = potlachRepository.getGift(giftID);
			if (gift != null) {	            
	            gift.setObscene(!gift.getObscene());
			}
			return gift;
	}
	
	
	@RequestMapping(value = ServerApi.GIFT_SEARCH)
	public @ResponseBody Collection<Gift> searchByTitle(
			@RequestHeader(value=ServerApi.HEADER_GIFT_TITLE)String title){
		
			return potlachRepository.findByTitle(title);
	}
	
	
	@RequestMapping(value = ServerApi.GIFT_CREATE, method = RequestMethod.POST)
	public @ResponseBody Gift createGift(
			@RequestHeader(value=ServerApi.BEARER_TOKEN)String token,
			@RequestHeader(value = ServerApi.HEADER_GIFT_CHAIN)Long giftChain,
			@RequestHeader(value = ServerApi.HEADER_GC_TITLE)String title,
			@RequestHeader(value = ServerApi.HEADER_GC_DESCRIPTION)String description,
			@RequestHeader(value = ServerApi.HEADER_GC_LATITUDE)Double latitude,
			@RequestHeader(value = ServerApi.HEADER_GC_LONGITUDE)Double longitude,
			@RequestHeader(value = ServerApi.HEADER_GC_PRECISION)Float precision,
			@RequestParam(ServerApi.GC_MULTI_IMAGE) MultipartFile image,
			@RequestParam(ServerApi.GC_MULTI_IMAGE_THUMB) MultipartFile imageThumb){	
		GiftCreator giftCreator = new GiftCreator();
		giftCreator.setTitle(title);
		giftCreator.setDescription(description);
		giftCreator.setLatitude(latitude);
		giftCreator.setLongitude(longitude);
		giftCreator.setPrecision(precision);
		try {
			giftCreator.setImageThumbBytes(imageThumb.getBytes());
			giftCreator.setImageBytes(image.getBytes());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				
		}
		gcmBroadcast.doBradcast();
		System.out.println("createGift");
		
		return potlachRepository.createGift(giftCreator, giftChain,token);
		
				
			
	}
	
	@RequestMapping(value = ServerApi.GIFT_DELETE)
	public @ResponseBody Boolean deleteGift(
			@RequestHeader(value=ServerApi.BEARER_TOKEN)String token,
			@RequestHeader(value = ServerApi.HEADER_GIFT_ID)Long giftID){	
		User user = potlachRepository.getUserWithToken(token);
		if (user != null){
			gcmBroadcast.doBradcast();
			return potlachRepository.deleteGift(giftID,user);
        }
		return false;
			
	}

	
	
}
