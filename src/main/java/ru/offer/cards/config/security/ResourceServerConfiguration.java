package ru.offer.cards.config.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final SecurityProperties securityProperties;

    public ResourceServerConfiguration(final TokenStore tokenStore, final SecurityProperties securityProperties) {
        this.tokenStore = tokenStore;
        this.securityProperties = securityProperties;
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (securityProperties.getEnabled()) {
            http.authorizeRequests().antMatchers("/api/**").authenticated();
        } else {
            http.authorizeRequests().antMatchers("/api/**").permitAll();
        }
    }
}