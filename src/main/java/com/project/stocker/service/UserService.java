package com.project.stocker.service;


import com.project.stocker.dto.request.LoginRequestDto;
import com.project.stocker.dto.request.SignupRequestDto;
import com.project.stocker.entity.User;
import com.project.stocker.entity.UserRoleEnum;
import com.project.stocker.jwt.JwtUtil;
import com.project.stocker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    long minute = 60 * 1000L;
    long hour = 60 * minute;
    private final long REFRESHTOKEN_TIME = hour;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("등록되지 않은 이메일입니다"));
        if(!user.isStatus()){
            return "해당 아이디가 비활성화 상태입니다.";
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());
        redisTemplate.opsForValue().set(refreshToken, user.getEmail());
        redisTemplate.opsForValue().set(user.getEmail(), refreshToken, 24, TimeUnit.HOURS);
        res.addHeader("RefreshToken", refreshToken);        //컨트롤러 단에서 해야함.
        return token;
    }



    public String logout(User user, HttpServletRequest req) {
        String token = jwtUtil.getJwtFromHeader(req);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String logoutToken = jwtUtil.logout(user.getEmail(), user.getRole());
        return logoutToken;
    }

    public void disabled(User user) {
        if(!user.isStatus()){
            throw new IllegalArgumentException("이미 비활성화된 상태입니다");
        }
        user.disabled();
    }
}