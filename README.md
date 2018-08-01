# spring-security-keycloak

This project uses spring-security-oauth2 a resource server to protect endpoints using Keycloak.

Video walkthrough here: https://drive.google.com/file/d/1Rt9BAbanDrgA2dglGXDqCSd3zatisBdM/view

## Getting up and running

### Start up keycloak

```
cd keycloak
docker-compose -f docker-compose-import.yaml up --force-recreate --build
```

### Start up the services

Terminal 1:

```
./start-service-1.sh
```

Terminal 2:

```
./start-service-2.sh
```

Terminal 3:

```
./start-service-3.sh
```

### Access service 1

Head over to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) in your browser.
Click the authorize button and it will use keycloak to get a JWT token, the default login is admin/password.

Once authorize you can execute the `/api/endpoint/forward` endpoint and you'll see your token being relayed through the 3 services.
