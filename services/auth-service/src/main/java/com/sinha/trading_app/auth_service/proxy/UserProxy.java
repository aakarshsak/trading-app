package com.sinha.trading_app.auth_service.proxy;

import com.sinha.trading_app.common.dto.ApiResponse;
import com.sinha.trading_app.common.dto.UserInfoRequest;
import com.sinha.trading_app.common.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8000")
public interface UserProxy {

    @PostMapping("/users")
    ResponseEntity<ApiResponse<UserInfoResponse>> addUser(@RequestBody UserInfoRequest request);

    @GetMapping("/users/{id}")
    ResponseEntity<ApiResponse<UserInfoResponse>> getUserByAuthId(@PathVariable("id") UUID userId);

}
