package com.project_letters_back.controller;

import com.project_letters_back.dto.UserDTO;
import com.project_letters_back.entity.User;
import com.project_letters_back.service.UserService;
import com.project_letters_back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

        if (!isValidEmail(userDTO.getEmail())) { // 이메일 형식 확인
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바른 이메일 주소를 입력하세요");
        }

        // 이메일 중복 확인
        boolean emailExists = userService.emailExists(userDTO.getEmail());
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다");
        }

        if (!isValidPassword(userDTO.getPassword())) { // 비밀번호 형식 확인
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("암호는 영문 대문자, 소문자, 숫자, 특수문자를 포함하는 8자리 이상이어야 합니다");
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) { // 비밀번호 일치 여부 확인
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다");
        }

        try {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setName(userDTO.getName());
            user.setGender(User.Gender.valueOf(userDTO.getGender()));
            user.setAge(userDTO.getAge());

            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공했습니다. 2초 후 로그인 페이지로 이동합니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류 발생: " + e.getMessage());
        }
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
        try {
            boolean authenticated = userService.authenticateUser(userDTO.getEmail(), userDTO.getPassword());
            if (authenticated) {
                String token = jwtUtil.generateToken(userDTO.getEmail());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "이메일, 암호가 일치하지 않습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace(); // 전체 스택 트레이스를 출력합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "로그인 중 오류가 발생했습니다. 관리자에게 문의하세요."));
        }
    }

    private boolean isValidEmail(String email) { // 이메일 형식 검증 로직
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) { // 비밀번호 형식 검증 로직
        // 최소 8자리, 대문자, 소문자, 숫자, 특수문자를 포함하는 정규식
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }
}
