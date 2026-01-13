package com.sinha.trading_app.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private String requestId;
}
