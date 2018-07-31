#!/usr/bin/env bash

SERVER_PORT=8082 \
MANAGEMENT_SERVER_PORT=7072 \
SPRING_APPLICATION_NAME="service-3" \
DOWNSTREAM_URL="http://localhost:8080/api/endpoint/forward" \
java -jar target/spring-security-keycloak-0.0.1.jar
