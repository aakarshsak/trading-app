package com.sinha.trading_app.auth_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min=2, max=50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min=2, max=50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=8, max=100, message = "Password must be between 0 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\+?[1-9]\\d{1,14}$", message = "Invalid mobile number format")
    private String mobileNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
}
