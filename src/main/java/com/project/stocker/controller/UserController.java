package com.project.stocker.controller;


import com.project.stocker.dto.request.LoginRequestDto;
import com.project.stocker.dto.request.SignupRequestDto;
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
    public void login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        String token = userService.login(requestDto);
        res.addHeader("Authorization", token);
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse res) {
        User user = userDetails.getUser();
        String logoutToken = userService.logout(user, request);
        res.addHeader("Authorization", logoutToken);
    }
}