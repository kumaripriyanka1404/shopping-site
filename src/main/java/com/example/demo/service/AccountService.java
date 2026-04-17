package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account create(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        
        return accountRepository.save(account);
    }

    public Optional<Account> update(long id, Account accountToBeUpdated){

        return accountRepository.findById(id).map(existing -> {
            existing.setFirstName(accountToBeUpdated.getFirstName());
            existing.setLastName(accountToBeUpdated.getLastName());
            existing.setEmail(accountToBeUpdated.getEmail());
            existing.setDeleted(accountToBeUpdated.isDeleted());

            if(StringUtils.hasText(accountToBeUpdated.getPassword())){
                existing.setPassword(passwordEncoder.encode(accountToBeUpdated.getPassword()));
            }

            return accountRepository.save(existing);
        });
    }

}
