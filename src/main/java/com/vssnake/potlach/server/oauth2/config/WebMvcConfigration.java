/**
 * 
 */
package com.vssnake.potlach.server.oauth2.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vssnake.potlach.server.ResourcesMapper;

/**
 * @author Kumar Sambhav Jain
 * 
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigration extends RepositoryRestMvcConfiguration {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//registry.addViewController("/login").setViewName("login");
		//registry.addViewController("/").setViewName("home");
		
	
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/photos/**").addResourceLocations(
	                "/photos/");
	}
	
	
	// We are overriding the bean that RepositoryRestMvcConfiguration 
		// is using to convert our objects into JSON so that we can control
		// the format. The Spring dependency injection will inject our instance
		// of ObjectMapper in all of the spring data rest classes that rely
		// on the ObjectMapper. This is an example of how Spring dependency
		// injection allows us to easily configure dependencies in code that
		// we don't have easy control over otherwise.
		//
		// Normally, we would not override this object mapping. However, in this
		// case, we are overriding the JSON conversion so that we can easily
		// extract a list of videos, etc. using Retrofit. You can remove this
		// method from the class to see what the default HATEOAS-based responses
		// from Spring Data Rest look like. You will need to access the server
		// from your browser as removing this method will break the Retrofit 
		// client.
		//
		// See the ResourcesMapper class for more details.
		@Override
		public ObjectMapper halObjectMapper(){
			return new ResourcesMapper();
		}
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
