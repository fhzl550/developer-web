package com.example.developerweb.core.auth.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LoginDto {
    private int userSeq;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhone;
    private Timestamp userBirthdate;
    private String userGender;
    private String userRole;
}
//userRole은 권한이다.
