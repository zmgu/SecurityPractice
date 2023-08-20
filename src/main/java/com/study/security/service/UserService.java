package com.study.security.service;

import com.study.security.domain.User;
import com.study.security.exception.AppException;
import com.study.security.exception.ErrorCode;
import com.study.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder encoder;

    public String join(String userName, String password){

        // userName 중복
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 있습니다.");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);


        return "성공";
    }

    public String  login(String  userName, String password) {


        return "token 리턴";
    }
}
