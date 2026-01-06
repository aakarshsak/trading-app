package com.sinha.trading_app.user_service.controller;


import com.sinha.trading_app.dto.ApiResponse;
import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import com.sinha.trading_app.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<UserInfoResponse>> addUser(@RequestBody UserInfoRequest request) {
        UserInfoResponse response = userService.addUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserInfoResponse>builder()
                        .status(HttpStatus.CREATED.toString())
                        .message("User created successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }




}
