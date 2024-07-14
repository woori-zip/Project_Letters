package com.project_letters_back.controller;

import com.project_letters_back.entity.User;
import com.project_letters_back.service.UserService;
import com.project_letters_back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KakaoController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/auth/kakao/callback")
    public RedirectView  kakaoCallback(@RequestParam String code) {
        System.out.println("Received Kakao callback with code: " + code);
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        // Access Token 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&code=" + code
                + "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        String accessToken = (String) response.getBody().get("access_token");

        // 사용자 정보 요청
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        Map<String, Object> userInfo = userInfoResponse.getBody();

        // 사용자 정보를 처리하는 로직 추가
        saveUser(userInfo);

        // 사용자 정보를 처리
        saveUser(userInfo);

        // 디버깅 로그 추가
        System.out.println("Access token to be returned: " + accessToken);

        // 토큰 반환
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", accessToken);

        // 사용자 정보를 처리하는 로직 추가
        String externalId = saveUser(userInfo);
        System.out.println("externalid:" + externalId);

        // 토큰 반환 대신 리디렉트
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8080/?token=" + accessToken + "&externalId=" + externalId);
        return redirectView;
    }

    @Autowired
    private UserService userService;

    private String saveUser(Map<String, Object> userInfo) {

        String kakaoId = userInfo.get("id").toString() + "@kakao";
        String nickname = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");

        System.out.println("Extracted kakaoId:" + kakaoId);
        System.out.println("Extracted nickname:" + nickname);

        // 이미 존재하는 사용자인지 확인
        User existingUser = userService.findByExternalId(kakaoId);
        if (existingUser != null) {
            System.out.println("이미 가입된 계정입니다.");
            return existingUser.getExternalId(); // 기존 사용자의 UUID 반환
        }

        // 사용자 정보 저장 로직
        User user = new User();
        user.setId(UUID.randomUUID().toString()); // 새로운 UUID 생성
        user.setExternalId(kakaoId); // 외부 사용자 ID 저장
        user.setEmail(""); // 이메일은 설정하지 않음
        user.setName(nickname);
        user.setPassword(""); // 비밀번호는 설정하지 않음
        user.setGender(User.Gender.other); // 성별 정보가 없는 경우 기본값 설정
        user.setAge(0); // 나이 정보가 없는 경우 기본값 설정
        userService.registerUser(user);

        System.out.println("kakao 회원가입 성공");
        return user.getExternalId(); // 새로운 사용자의 externalId 반환
    }

    @GetMapping("/auth/kakao/logout")
    public ResponseEntity<String> kakaoLogout(@RequestParam String token) {
        // 카카오 로그아웃 처리
        String logoutUri = "https://kapi.kakao.com/v1/user/logout";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(logoutUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Successfully logged out from Kakao");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to log out from Kakao");
        }
    }
}
