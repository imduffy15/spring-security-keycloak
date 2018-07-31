#!/usr/bin/env bash

SERVER_PORT=8081 \
MANAGEMENT_SERVER_PORT=7071 \
SPRING_APPLICATION_NAME="service-2" \
DOWNSTREAM_URL="http://localhost:8082/api/endpoint/base" \
java -jar target/spring-security-keycloak-0.0.1.jar
