package ie.ianduffy.poc.spring.security.keycloak.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableResourceServer
@Import(SecurityProblemSupport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;

    private final String name;

    private final List<String> jwtCertUris;

    @Bean
    public TokenStore tokenStore() {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();

        defaultAccessTokenConverter.setUserTokenConverter(new TenantAwareUserAuthenticationConverter());

        return new JwkTokenStore(jwtCertUris, defaultAccessTokenConverter, null);
    }

    @Autowired
    public ResourceServerConfiguration(
        SecurityProblemSupport problemSupport,
        @Value("${security.aud}") String name,
        @Value("${security.jwk-cert-uris}") String[] jwtCertUris
    ) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        this.problemSupport = problemSupport;
        this.name = name;
        this.jwtCertUris = Arrays.asList(jwtCertUris);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(problemSupport);
        resources.accessDeniedHandler(problemSupport);
        resources.resourceId(name);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/api/config").permitAll()
            .antMatchers("/**").permitAll();
        ;
    }
}
