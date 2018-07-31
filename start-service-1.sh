#!/usr/bin/env bash

SERVER_PORT=8080 \
MANAGEMENT_SERVER_PORT=7070 \
SPRING_APPLICATION_NAME="service-1" \
DOWNSTREAM_URL="http://localhost:8081/api/endpoint/forward" \
java -jar target/spring-security-keycloak-0.0.1.jar
