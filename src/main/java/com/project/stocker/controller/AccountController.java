package com.project.stocker.controller;

import com.project.stocker.dto.request.AccountAddStockRequest;
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
    @GetMapping("/user/{userId}/account/get")
    public List<AccountResponseDto> getMyAccount(@PathVariable Long userId){
        return accountService.getMyAccount(userId);
    }
    @PutMapping("/user/{userId}/account/addStock")
    public ResponseEntity<String> addStock(@PathVariable Long userId, @RequestBody AccountAddStockRequest requestDto){
        accountService.addStock(userId, requestDto);
        return ResponseEntity.ok("성공적으로 주식을 추가했습니다.");
    }
}
