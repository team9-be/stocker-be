package com.project.stocker.service;

import com.project.stocker.dto.response.AccountResponseDto;
import com.project.stocker.entity.Account;
import com.project.stocker.entity.Trade;
import com.project.stocker.entity.User;
import com.project.stocker.repository.AccountRepository;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountResponseDto> getMyAccount(Long userId) {
       return accountRepository.findAllByUserId(userId).stream().map(AccountResponseDto::new).toList();
    }

    public void createAccount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
            Account account = new Account(user);
            accountRepository.save(account);
        }
    @Transactional
    public void updateAccount(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        List<Trade> buys = user.getBuys();
        for (Trade buy : buys) {
            if(accountRepository.findByUserIdAndStockCompany(userId,buy.getStock().getCompany()).isEmpty()
                    && buy.getStatus().equals("confirm")){
                Account account1 = new Account(user,buy);
                accountRepository.save(account1);
            }
            else if(!accountRepository.findByUserIdAndStockCompany(userId,buy.getStock().getCompany()).isEmpty()
            && buy.getStatus().equals("confirm")){
                Account account2 = accountRepository.findByUserIdAndStockCompany(userId, buy.getStock().getCompany()).get();
                account2.update(buy);
            }
        }
    }
}
