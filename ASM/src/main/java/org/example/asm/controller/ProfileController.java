package org.example.asm.controller;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.asm.dto.ApiResponse;
import org.example.asm.dto.request.LoginRequest;
import org.example.asm.dto.request.RegisterRequest;
import org.example.asm.dto.response.LoginResponse;
import org.example.asm.dto.response.ProfileResponse;
import org.example.asm.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping("/register")
    ApiResponse<ProfileResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .data(profileService.register(request))
                .code(200)
                .build();
    }

    @GetMapping("/profiles")
    ApiResponse<List<ProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .data(profileService.getAllProfiles())
                .code(200)
                .build();
    }

    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getMyProfile() {
        return ApiResponse.<ProfileResponse>builder()
                .data(profileService.getMyProfile())
                .code(200)
                .build();
    }

    @PostMapping("/login")
    ApiResponse<LoginResponse> Login(@RequestBody LoginRequest request) {
        var response = profileService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .data(response)
                .build();
    }
}
