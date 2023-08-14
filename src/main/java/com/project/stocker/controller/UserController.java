package com.project.stocker.controller;


import com.project.stocker.dto.request.LoginRequestDto;
import com.project.stocker.dto.request.SignupRequestDto;
import com.project.stocker.dto.response.UserResponse;
import com.project.stocker.entity.User;
import com.project.stocker.filter.UserDetailsImpl;
import com.project.stocker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        String token = userService.login(requestDto);
        if(token.equals("해당 아이디가 비활성화 상태입니다.")){
            return new UserResponse("해당 아이디가 비활성화 상태입니다.");
        }
        res.addHeader("Authorization", token);
        return new UserResponse("로그인 되었습니다");
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse res) {
        User user = userDetails.getUser();
        String logoutToken = userService.logout(user, request);
        res.addHeader("Authorization", logoutToken);
    }
    @PostMapping("/disabled")
    public UserResponse disabled(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        userService.disabled(user);
        return new UserResponse("비활성화 되었습니다");
    }
}