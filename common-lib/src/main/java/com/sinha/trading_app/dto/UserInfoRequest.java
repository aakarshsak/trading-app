package com.sinha.trading_app.dto;


import com.sinha.trading_app.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private LocalDate dob;
    private Gender gender;
    private String nationality;
}
