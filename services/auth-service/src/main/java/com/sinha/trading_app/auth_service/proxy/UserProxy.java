package com.sinha.trading_app.auth_service.proxy;

import com.sinha.trading_app.dto.ApiResponse;
import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service", path = "http://localhost:8000/users")
public interface UserProxy {

    @PostMapping("/")
    ResponseEntity<ApiResponse<UserInfoResponse>> addUser(@RequestBody UserInfoRequest request);

    @PostMapping("/{id}")
    ResponseEntity<ApiResponse<UserInfoResponse>> getUserByAuthId(@PathVariable UUID authId);

}
