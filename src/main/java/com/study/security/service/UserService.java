package com.study.security.service;

import com.study.security.domain.User;
import com.study.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;

    public String join(String userName, String password){

        // userName 중복
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new RuntimeException(userName + "는 이미 있습니다.");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();
        userRepository.save(user);


        return "성공";
    }
}
