package com.virag.finedge.service;

import com.virag.finedge.dto.request.LoginRequest;
import com.virag.finedge.dto.request.RegisterRequest;
import com.virag.finedge.dto.response.ApiResponse;
import com.virag.finedge.dto.response.LoginResponse;
import com.virag.finedge.dto.response.UserProfileResponse;

public interface UserService {

    ApiResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserProfileResponse getProfile(String email);
}