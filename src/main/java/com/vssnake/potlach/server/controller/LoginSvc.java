package com.vssnake.potlach.server.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vssnake.potlach.server.GCMBroadcast;
import com.vssnake.potlach.server.ServerApi;
import com.vssnake.potlach.server.model.User;
import com.vssnake.potlach.server.repository.PotlachRepository;
import com.vssnake.potlach.server.repository.TokenRepository;
import com.vssnake.utils.network.AbstractHttpsClient;
import com.vssnake.utils.network.JettyHttpsClient;
import com.vssnake.utils.network.rest.GoogleOauth;
import com.vssnake.utils.network.rest.GoogleOauth.UserInfoHandler;
import com.vssnake.utils.network.rest.Oauth2RestBuilder;
import com.vssnake.utils.network.rest.model.OauthGoogleError;
import com.vssnake.utils.network.rest.model.OauthGoogleUserInfo;

@Controller
public class LoginSvc {
	
	@Autowired
	PotlachRepository potlachRepository;
	
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	AbstractHttpsClient httpClient;
	
	@Autowired 
	AuthenticationManager authManager;
	
	@Autowired
	GCMBroadcast gcmBroadcast;
	
	
	@RequestMapping(value = ServerApi.LOGIN)
	public @ResponseBody User loginUser(
			@RequestHeader(value=ServerApi.HEADER_EMAIL)String email,
			@RequestHeader(value=ServerApi.BEARER_TOKEN) String token,
			@RequestHeader(value=ServerApi.HEADER_GCM_CLIENT_KEY)String clientKey,
			HttpServletResponse response,
			HttpServletRequest request){
	
		User user = potlachRepository.getUser(email);
		if (user == null){
			log("user null");
			user = createUser(email,token);
			
			if (user == null){
				log("create User Failed");
				response.setStatus(401);
				return null;
			}
			log("Save User");
			potlachRepository.addUser(user);
			tokenRepository.saveCredentials(email, token);
			//setPrincipal(user, request);
			log("Save Principal");
			gcmBroadcast.addAndroidTarget(clientKey);
			return user;
		}else{
			log("Check User Exist");
			if(tokenRepository.checkCredentials(email, token)){
				log("User has Credentials");
				//setPrincipal(user, request);
				return user;
			}else{
				if (checkWithGoogleAuth(email, token)){
					user.setToken(token);
					tokenRepository.saveCredentials(email, token);
					log("Google Auth credentials accepted");
					//setPrincipal(user, request);
					gcmBroadcast.addAndroidTarget(clientKey);
					return user;
				}else{
					log("User dont have credentials");
				}
				
				
			}
		}
		return null;
	}
	
	@RequestMapping(value= ServerApi.LOGOUT)
	public @ResponseBody boolean logout(
			@RequestHeader(ServerApi.BEARER_TOKEN) String token,
			@RequestHeader(value=ServerApi.HEADER_GCM_CLIENT_KEY)String clientKey){
		User user = potlachRepository.getUserWithToken(token);
		if (user == null){
			return false;
		}else{
			tokenRepository.removeUser(user.getEmail(), token);
			gcmBroadcast.removeAndroidTarget(clientKey);
			return true;
		}
	}
	
	
	private boolean checkWithGoogleAuth(final String email, final String token){
		Oauth2RestBuilder restBuilder = new Oauth2RestBuilder(JettyHttpsClient.getHttpClient());
		GoogleOauth googleOauth = new GoogleOauth(restBuilder);
		int result = googleOauth.getUserInfo(token);
		
		if (result < 400){
			if (email.contentEquals(googleOauth.googleUserInfo.getEmail())){
				return true;
			}
		}
		return false;
	}
	
	private User createUser(final String email,final String token){
		Oauth2RestBuilder restBuilder = new Oauth2RestBuilder(JettyHttpsClient.getHttpClient());
		GoogleOauth googleOauth = new GoogleOauth(restBuilder);
		int result = googleOauth.getUserInfo(token);
		
		if (result < 400){
			if (email.contentEquals(googleOauth.googleUserInfo.getEmail())){
				User user =new User(email,
						googleOauth.googleUserInfo.getName(),
						false,
						token,
						null,
						googleOauth.googleUserInfo.getPicture());		
				return user;
			}else if (result > 0){
				return null;
			}else{
				return null;
			}
		}
		return null;

	}
	
	private void log(String text){
		System.out.println(text);
	}

}
