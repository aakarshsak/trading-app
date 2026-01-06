package com.sinha.trading_app.user_service.service;

import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import com.sinha.trading_app.enums.UserStatus;

import java.util.UUID;

public interface UserService {

    UserInfoResponse addUser(UserInfoRequest user);

    UserInfoResponse getUser(UUID id) throws Exception;

    UserInfoResponse updateUser(UUID id, UserInfoRequest user) throws Exception;

    void updateUserStatus(UUID id, UserStatus userStatus);
}
