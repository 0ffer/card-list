package ru.offer.cards.config.security;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.IOException;
import java.security.KeyPair;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;

    public AuthorizationServerConfiguration(final PasswordEncoder passwordEncoder,
                                            final AuthenticationManager authenticationManager,
                                            final SecurityProperties securityProperties) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        val jwtAccessTokenConverter = new JwtAccessTokenConverter();

        val keyPair = getKeyPair();

        jwtAccessTokenConverter.setKeyPair(keyPair);

        // from Resourse server config
        jwtAccessTokenConverter.setVerifierKey(getPublicKeyAsString());

        return jwtAccessTokenConverter;
    }

    private KeyPair getKeyPair() {
        val jwtProperties = securityProperties.getJwt();

        val keyStorePasswordBytes = jwtProperties.getKeyStorePassword().toCharArray();
        val keyStoreKeyFactory = new KeyStoreKeyFactory(jwtProperties.getKeyStore(), keyStorePasswordBytes);

        val keyPairPasswordBytes = jwtProperties.getKeyPairPassword().toCharArray();
        return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(), keyPairPasswordBytes);
    }

    @SneakyThrows(IOException.class)
    private String getPublicKeyAsString() {
        return IOUtils.toString(securityProperties.getJwt().getPublicKey().getInputStream(), UTF_8);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public DefaultTokenServices tokenServices(final ClientDetailsService clientDetailsService) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(this.authenticationManager);
        return tokenServices;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("ClientId")
                .secret("{noop}secret")
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token", "client_credentials")
                .authorities("ROLE_CLIENT")
                .accessTokenValiditySeconds(300);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.passwordEncoder(this.passwordEncoder).tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
}
