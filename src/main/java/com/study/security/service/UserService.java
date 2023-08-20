package com.study.security.service;

import com.study.security.domain.User;
import com.study.security.exception.AppException;
import com.study.security.exception.ErrorCode;
import com.study.security.repository.UserRepository;
import com.study.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder encoder;
   @Value("${security.jwt.token.secret}")
   private String secret;

   private final Long expireTimeMs = 1000 * 60 * 60l;
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
        // userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        // password 틀림
        if (!encoder.matches(password, selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력 했습니다.");
        }

        // 앞에서 Exception 안나면 토큰 발행
        return JwtUtil.createToken(selectedUser.getUserName(), secret, expireTimeMs);
    }
}
