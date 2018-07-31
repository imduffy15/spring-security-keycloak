package ie.ianduffy.poc.spring.security.keycloak.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Optional;

@Configuration
public class OAuth2RestTemplateConfiguration {
    @Bean
    public OAuth2ClientContext oauth2ClientContext() {
        return new DefaultOAuth2ClientContext() {
            private static final long serialVersionUID = -7563661585547122665L;
            @Override
            public OAuth2AccessToken getAccessToken() {
                return Optional.ofNullable(super.getAccessToken())
                    .orElse(new DefaultOAuth2AccessToken(
                        ((OAuth2AuthenticationDetails) SecurityContextHolder
                            .getContext().getAuthentication().getDetails())
                            .getTokenValue()));
            }

        };
    }

    @Bean
    public OAuth2ProtectedResourceDetails oauth2ProtectedResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ClientContext oAuth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oAuth2ClientContext);
    }
}
