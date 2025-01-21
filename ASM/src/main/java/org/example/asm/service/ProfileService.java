package org.example.asm.service;


import feign.FeignException;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.example.asm.dto.keycloak.LoginRequestParam;
import org.example.asm.dto.request.LoginRequest;
import org.example.asm.dto.request.RegisterRequest;
import org.example.asm.dto.response.LoginResponse;
import org.example.asm.dto.response.ProfileResponse;
import org.example.asm.exception.AppException;
import org.example.asm.exception.ErrorCode;
import org.example.asm.exception.ErrorNornalizer;
import org.example.asm.mapper.ProfileMapper;
import org.example.asm.model.Profile;
import org.example.asm.repository.KeycloakRepository;
import org.example.asm.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileService {

    ProfileRepository profileRepository;
    KeycloakRepository keycloakRepository;
    ProfileMapper profileMapper;
    ErrorNornalizer errorNornalizer;
    PasswordEncoder passwordEncoder;

//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    //lấy tất cả thông tin profile
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    //lấy thông tin profile của user hiện tại
    public ProfileResponse getMyProfile() {
      var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var profile = profileRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return profileMapper.toProfileResponse(profile);
    }

    //đăng ký tài khoản
    public ProfileResponse register(RegisterRequest request) {
        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // Tạo người dùng mới và lưu vào cơ sở dữ liệu
        Profile user = new Profile();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encryptedPassword);

        user = profileRepository.save(user);


        // Chuyển đổi thành response nếu cần
        return profileMapper.toProfileResponse(user);
    }

    //lấy userId từ response
    private String extractUserId(ResponseEntity<?> response) {
        // Extract userId from response
        String location = response.getHeaders().get("Location").get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    public LoginResponse login(LoginRequest request) {
        try {
            var token = keycloakRepository.exchangeToken(LoginRequestParam.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .scope("openid")
                    .build());
            System.out.println("Token Response: " + token);
            System.out.println("Access Token: " + token.getAccessToken());
            System.out.println("Refresh Token: " + token.getRefreshToken());

            return LoginResponse.builder()
//                    .preferredUsername(token.getPreferredUsername())
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .build();
        } catch (FeignException e) {
            throw errorNornalizer.handleKeyCloakException(e);
        }
    }


}
