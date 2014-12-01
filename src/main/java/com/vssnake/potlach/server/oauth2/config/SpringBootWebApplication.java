package com.vssnake.potlach.server.oauth2.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.MultipartConfigElement;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vssnake.potlach.server.GCMBroadcast;
import com.vssnake.potlach.server.filter.TestFilter;
import com.vssnake.potlach.server.repository.MemoryPotlachRepository;
import com.vssnake.potlach.server.repository.MemoryTokenRepository;
import com.vssnake.potlach.server.repository.PotlachRepository;
import com.vssnake.potlach.server.repository.TokenRepository;
import com.vssnake.utils.network.AbstractHttpsClient;
import com.vssnake.utils.network.JettyHttpsClient;
import com.vssnake.utils.network.rest.GoogleOauth;
import com.vssnake.utils.network.rest.GoogleOauth.UserInfoHandler;
import com.vssnake.utils.network.rest.Oauth2RestBuilder;
import com.vssnake.utils.network.rest.model.OauthGoogleError;
import com.vssnake.utils.network.rest.model.OauthGoogleUserInfo;

@Configuration
@ComponentScan(basePackages = { "com.vssnake" })
@EnableAutoConfiguration
//Tell Spring to automatically create a JPA implementation of our
//VideoRepository

public class SpringBootWebApplication implements
		EmbeddedServletContainerCustomizer {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebApplication.class, args);
		
	}
	
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5048KB");
        factory.setMaxRequestSize("5048KB");
        return factory.createMultipartConfig();
    }

	@Bean
	public JettyServerCustomizer jettyServerCustomizer() {
		return new JettyServerCustomizer() {

			@Override
			public void customize(Server server) {
				
			
				SslContextFactory sslContextFactory = new SslContextFactory();
                sslContextFactory.setKeyStorePassword("jetty6");
                try {
                    sslContextFactory.setKeyStorePath(ResourceUtils.getFile(
                            "classpath:jetty-ssl.keystore").getAbsolutePath());
                }
                catch (FileNotFoundException ex) {
                    throw new IllegalStateException("Could not load keystore", ex);
                }
                SslSocketConnector sslConnector = new SslSocketConnector(
                        sslContextFactory);
                sslConnector.setPort(9993);
                sslConnector.setMaxIdleTime(60000);
                server.addConnector(sslConnector);
				
				/*SslSocketConnector sslConnector = new SslSocketConnector(
						sslContextFactory);
				sslConnector.setPort(8080);
				sslConnector.setMaxIdleTime(60000);
				server.add
				server.addConnector(sslConnector);*/
			}
		};
	}

	public void customizeJetty(
			JettyEmbeddedServletContainerFactory containerFactory) {
		containerFactory.addServerCustomizers(jettyServerCustomizer());
	}
	
	

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (container instanceof JettyEmbeddedServletContainerFactory) {
			customizeJetty((JettyEmbeddedServletContainerFactory) container);
		}
	}
	
	@Bean
	public  TokenRepository tokenRepository(){
		return MemoryTokenRepository.getInstance();
	}

	@Bean
	public  PotlachRepository potlachRepository(){
		return new MemoryPotlachRepository();
	}


	@Bean GCMBroadcast gcmBroadCast(){
		return new GCMBroadcast();
	}
	
	
	
	@Bean 
	public AbstractHttpsClient httpsClient(){
		return JettyHttpsClient.getHttpClient();
	}
	
	/*@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		TestFilter securityFilter = new TestFilter();
		
		registrationBean.setFilter(securityFilter);
		registrationBean.setOrder(2);
		return registrationBean;
	}*/
	
	
}
