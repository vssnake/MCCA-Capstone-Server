package com.vssnake.potlach.server.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vssnake.potlach.server.ServerApi;
import com.vssnake.potlach.server.model.Gift;
import com.vssnake.potlach.server.model.SpecialInfo;
import com.vssnake.potlach.server.repository.PotlachRepository;

@Controller
public class SpecialInfoSvc {

	@Autowired
	private PotlachRepository potlachRepository;
	
	@RequestMapping(value = ServerApi.SPECIAL_INFO)
	public @ResponseBody SpecialInfo getGifts(){
		return potlachRepository.showSpecialInfo();	
	}
}
