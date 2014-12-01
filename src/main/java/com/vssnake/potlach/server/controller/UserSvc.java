package com.vssnake.potlach.server.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;

import com.vssnake.potlach.server.ServerApi;
import com.vssnake.potlach.server.model.User;
import com.vssnake.potlach.server.repository.PotlachRepository;

@Controller
public class UserSvc {

	
	@Autowired
	PotlachRepository potlachRepository;
	
	@RequestMapping(value = ServerApi.GET_USER)
	public @ResponseBody User getUser(@RequestHeader(ServerApi.HEADER_EMAIL)String email,
			@RequestHeader(value = ServerApi.BEARER_TOKEN) String token){
		return potlachRepository.getUser(email);
	}
	
	@RequestMapping(value = ServerApi.CURRENT_USER_MODIF_INA)
	public @ResponseBody boolean modifyInna(
			@RequestHeader(value = ServerApi.BEARER_TOKEN) String token,
			@RequestHeader (value = ServerApi.HEADER_INNA) Boolean inappropriate){
		
		
		User user =  potlachRepository.getUserWithToken(token);
		user.setHideInappropriate(inappropriate);
		return inappropriate;
	}
}
