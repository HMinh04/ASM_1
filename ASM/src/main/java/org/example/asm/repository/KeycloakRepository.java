package org.example.asm.repository;

import feign.QueryMap;
import org.example.asm.dto.keycloak.LoginRequestParam;
import org.example.asm.dto.keycloak.TokenExchangeParam;
import org.example.asm.dto.keycloak.TokenExchangeResponse;
import org.example.asm.dto.keycloak.UserCreationParam;
import org.example.asm.dto.response.LoginResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface KeycloakRepository {
    @PostMapping(value = "/realms/HMinh/protocol/openid-connect/token",
            consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/HMinh/users",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationParam param);

    @PostMapping(value = "/realms/HMinh/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse exchangeToken(@QueryMap LoginRequestParam param);

}

