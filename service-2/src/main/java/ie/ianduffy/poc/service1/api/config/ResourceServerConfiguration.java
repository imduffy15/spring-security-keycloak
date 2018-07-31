package ie.ianduffy.poc.service1.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableResourceServer
@Import(SecurityProblemSupport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;

    private final String name;

    @Autowired
    public ResourceServerConfiguration(SecurityProblemSupport problemSupport, @Value("${spring.application.name}") String name) {
        this.problemSupport = problemSupport;
        this.name = name;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(problemSupport);
        resources.accessDeniedHandler(problemSupport);
        resources.resourceId(name);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/api/config").permitAll()
            .antMatchers("/**").permitAll();
        ;
    }
}
