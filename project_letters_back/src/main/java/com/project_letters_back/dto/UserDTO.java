package com.project_letters_back.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String confirmPassword;  // 비밀번호 확인을 위한 필드
    private String name;
    private String gender;
    private int age;
}