package com.project.stocker.controller;

import com.project.stocker.dto.response.AccountResponseDto;
import com.project.stocker.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/user/{userId}/account/get")
    public List<AccountResponseDto> getMyAccount(@PathVariable Long userId){
        return accountService.getMyAccount(userId);
    }
}
