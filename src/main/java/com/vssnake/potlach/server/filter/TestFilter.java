package com.vssnake.potlach.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;

import com.vssnake.potlach.server.repository.TokenRepository;


public class TestFilter extends GenericFilterBean {


	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse)arg1;
		HttpServletRequest httpRequest = (HttpServletRequest)arg0;

		System.out.println(httpRequest.getContentType());
		arg2.doFilter(arg0, arg1);
		
		/*tokenID =httpRequest.getParameter("Token");
		userID =tokenRepository.getEmail(tokenID);
		
		if (userID != null || tokenID != null){
			if (tokenRepository.checkCredentials(userID, tokenID)){			
				arg2.doFilter(arg0, arg1);
			}else{
				httpResponse.sendError(401);
			}	
		}else{
			httpResponse.sendError(401);
		}*/

	}

}
