package com.foodrecommendation.food.service;

import com.foodrecommendation.food.entity.User;
import com.foodrecommendation.food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public void register(String username, String rawPassword) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword)) // 비밀번호 암호화
                .role("ROLE_USER") // 기본 권한
                .build();
        userRepository.save(user);
    }
}
