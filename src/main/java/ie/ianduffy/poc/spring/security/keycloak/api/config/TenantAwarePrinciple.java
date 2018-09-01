package ie.ianduffy.poc.spring.security.keycloak.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TenantAwarePrinciple {
    private final Object principle;
    private final String tenant;
}
