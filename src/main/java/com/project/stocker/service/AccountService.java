package com.project.stocker.service;

import com.project.stocker.dto.response.AccountResponseDto;
import com.project.stocker.entity.Account;
import com.project.stocker.entity.Trade;
import com.project.stocker.entity.User;
import com.project.stocker.repository.AccountRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<AccountResponseDto> getMyAccount(Long userId) {
        return accountRepository.findAllByUserId(userId).stream().map(AccountResponseDto::new).toList();
    }
}
