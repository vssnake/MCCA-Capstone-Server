/**
 * 
 */
package com.vssnake.potlach.server.oauth2.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import com.sun.org.apache.xpath.internal.operations.And;
import com.vssnake.potlach.server.filter.TestFilter;

/**
 * Configuration class for security
 * 
 * @author vssnake
 * 
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	  @Override
	    public void configure(WebSecurity webSecurity) throws Exception
	    {
	        webSecurity
	            .ignoring()
	                // All of Spring Security will ignore the requests
	                .antMatchers("/**");
	    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter
	 * #configure(org.springframework.security.config
	 * .annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	

		/*http.csrf().disable();
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST).hasAuthority("ROLE_USER");*/
		/*http
		.authorizeRequests()
			.antMatchers(HttpMethod.POST).permitAll()
			.antMatchers("/oauth/registration").anonymous()
			.and()
				.requiresChannel().anyRequest().requiresSecure();*/

		/*http.requiresChannel().anyRequest().requiresSecure()
		.and()
			.antMatcher("/**")
				.addFilterAfter(new TestFilter(),ExceptionTranslationFilter.class);*/
		
		http
        .addFilterAfter(new TestFilter(),ExceptionTranslationFilter.class)
        .authorizeRequests()
            // this will grant access to GET /login too do you really want that?
            .antMatchers("/**").permitAll();
		/*http.authorizeRequests()
						.antMatchers(HttpMethod.POST,"/potlach").hasAuthority("ROLE_USER");*/
		
		/*http.requiresChannel().anyRequest().requiresSecure()
		.and()
			.antMatcher("/potlach")
				.addFilterAfter(testFilter(),ExceptionTranslationFilter.class);*/
					
		
		//http.authorizeRequests().antMatchers("/potlach").hasAnyAuthority("ROLE_USER");
		
						
		
	}
}

	