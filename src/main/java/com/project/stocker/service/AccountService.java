package com.project.stocker.service;

import com.project.stocker.dto.request.AccountAddStockRequest;
import com.project.stocker.dto.response.AccountResponseDto;
import com.project.stocker.entity.Account;
import com.project.stocker.repository.AccountRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountResponseDto> getMyAccount(Long userId) {
        return accountRepository.findAllByUserId(userId).stream().map(AccountResponseDto::new).toList();
    }

    public void addStock(Long userId, AccountAddStockRequest requestDto) {
        if(accountRepository.findByUserIdAndStockCompany(userId, requestDto.getStockCompany()).isEmpty()){
            Account account = new Account(userRepository.findById(userId).get(), requestDto);
            accountRepository.save(account);
        }
        else {
            Account account = accountRepository.findByUserIdAndStockCompany(userId,
                    requestDto.getStockCompany()).get();
            account.changeQuantity(requestDto.getQuantity());
        }
    }
}
