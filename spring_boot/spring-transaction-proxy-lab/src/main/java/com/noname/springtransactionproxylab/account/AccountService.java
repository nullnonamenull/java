package com.noname.springtransactionproxylab.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void create(String name) {
        var account = new Account();
        account.setId(UUID.randomUUID());
        account.setAmount(BigDecimal.ZERO);
        account.setName(name);
        accountRepository.save(account);
    }

}
