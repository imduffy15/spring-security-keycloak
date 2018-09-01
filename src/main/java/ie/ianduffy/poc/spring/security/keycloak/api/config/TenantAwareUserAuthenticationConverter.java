package ie.ianduffy.poc.spring.security.keycloak.api.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.io.File;
import java.net.URI;
import java.util.Map;

public class TenantAwareUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication authentication = super.extractAuthentication(map);

        String tenant = "default";
        if(map.containsKey("iss")) {
            tenant = new File(URI.create((String) map.get("iss")).getPath()).getName();
        }

        return new UsernamePasswordAuthenticationToken(
            new TenantAwarePrinciple(authentication.getPrincipal(), tenant),
            authentication.getCredentials(),
            authentication.getAuthorities()
        );
    }
}
