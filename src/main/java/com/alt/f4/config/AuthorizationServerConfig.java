package com.alt.f4.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.alt.f4.config.property.ProdutoApiProperty;
import com.alt.f4.config.token.CustomTokenEnhancer;



@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ProdutoApiProperty property;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("desktop")
				.secret("$2a$10$dwjdpjVmQQbKQWZl2.mtK.poLi/OPyLdis8swKAJtIvOMXm4dRv/e") //d3skt0p0
				.scopes("read", "write")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(property.getToken().getTokenExpirado()) // 24 horas
				.refreshTokenValiditySeconds(property.getToken().getRefreshTokenExpirado()); // 7 dias
	}
	
	/*
	 * security/usuario-sistema
	 * security/app-user-details-service
	 * config.token/custom-token-enhancer
	 * config/oauth-security-config
	 * config/resource-server-config
	 * config/authorization-server-config
	 */
	
	/*
	 * .../oauth/token -> username={}&password={}&grant_type=password
	 * 
	 *  Para usar sem refresh token pelo cookie:
	 *  - TokenResouce 
	 *  - RefreshTokenCookiePreProcessorFilter
	 *  - RefreshTokenPostProcessor
	 *  .../oauth/token -> grant_type=refresh_token & refresh_token={código-retornado-na-request-password}
	 *
	 * Para usar com refresh token pelo cookie
	 * .../oauth/token -> grant_type=refresh_token
	 */
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
		.tokenStore(tokenStore())
		.tokenEnhancer(tokenEnhancerChain)
		.reuseRefreshTokens(false)
		.userDetailsService(this.userDetailsService)
		.authenticationManager(this.authenticationManager);
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(property.getToken().getSegredo());
		return accessTokenConverter;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}
}
