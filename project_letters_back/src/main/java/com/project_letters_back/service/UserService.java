package com.project_letters_back.service;

import com.project_letters_back.entity.User;
import com.project_letters_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    public boolean emailExists(String email) {
        System.out.println("Checking if email exists: " + email); // 디버그 메시지 추가
        Optional<User> userOptional = userRepository.findByEmail(email);
        boolean exists = userOptional.isPresent();
        System.out.println("Email exists: " + exists); // 디버그 메시지 추가
        return exists;
    }

    public boolean authenticateUser(String email, String password) {
        try {
            System.out.println("사용자 인증 시도: " + email); // 로그 추가
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                boolean matches = passwordEncoder.matches(password, user.getPassword());
                System.out.println("비밀번호 일치 여부: " + matches); // 로그 추가
                return matches;
            }
            System.out.println("사용자를 찾을 수 없음: " + email); // 로그 추가
            return false;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 스택 트레이스를 콘솔에 출력
            throw e; // 예외를 다시 던짐
        }
    }

    public User findByExternalId(String externalId) {
        return userRepository.findByExternalId(externalId);
    }
}
