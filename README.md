[This project is a WIP](http://www.repostatus.org/badges/latest/wip.svg "wip badge")

# spring-security-keycloak

This project uses spring-security-oauth2 a resource server to protect endpoints using Keycloak.

## Components

### Resource servers

#### Service-1, Service-2, Service-3

All of these services have the same resource server configuration and demonstrate token relaying.

### Keycloak

Keycloak acts as the authorization service. It will generate the tokens used to access our application.

## Description

This example gives an endpoint at service-1:8080, that calls another endpoint at service-2:8081, which finally calls service-3 which gives a response to the initial request.

The call to service-1 uses a JWT acces token passed via the `Authorization` header with type `Bearer`. The resource server validates the token and exposes it via the security context. The `OAuth2RestTemplate` relays this JWT to the downstream endpoint service-2:8081, which relays the request to service-3:8082 which responses to the initial request.
