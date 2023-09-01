package com.project.stocker.controller;

import com.project.stocker.dto.response.AccountResponseDto;
import com.project.stocker.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;
    @PostMapping("/user/{userId}/account/create")
    public ResponseEntity<String> createAccount(@PathVariable Long userId){
        accountService.createAccount(userId);
        return ResponseEntity.ok("계좌가 생성되었습니다");
    }
    @GetMapping("/user/{userId}/account/get")
    public List<AccountResponseDto> getMyAccount(@PathVariable Long userId){
        return accountService.getMyAccount(userId);
    }

    @PutMapping("/user/{userId}/account/update")
    public ResponseEntity<String> updateAccount(@PathVariable Long userId){
        accountService.updateAccount(userId);
        return ResponseEntity.ok("업데이트 되었습니다.");
    }
}
