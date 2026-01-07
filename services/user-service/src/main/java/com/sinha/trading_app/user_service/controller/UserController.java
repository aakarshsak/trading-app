package com.sinha.trading_app.user_service.controller;


import com.sinha.trading_app.dto.ApiResponse;
import com.sinha.trading_app.dto.MessageResponse;
import com.sinha.trading_app.dto.UserInfoRequest;
import com.sinha.trading_app.dto.UserInfoResponse;
import com.sinha.trading_app.enums.UserStatus;
import com.sinha.trading_app.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

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


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(@PathVariable("id") UUID id) throws Exception {
        UserInfoResponse response = userService.getUser(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<UserInfoResponse>builder()
                        .status(HttpStatus.OK.toString())
                        .message("Retrieved user successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUser(@PathVariable("id") UUID id, @RequestBody UserInfoRequest request) throws Exception {
        UserInfoResponse response = userService.updateUser(id, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<UserInfoResponse>builder()
                        .status(HttpStatus.OK.toString())
                        .message("Updated user successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MessageResponse> updateUserStatus(@PathVariable("id") UUID id, @RequestParam("status") UserStatus status) {
        userService.updateUserStatus(id, status);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(MessageResponse.builder().build());
    }


}
