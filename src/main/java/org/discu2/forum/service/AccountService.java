package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public void registerNewAccount(Account account) {
        System.out.println("reg!");

        repository.findAccountsByMail(account.getMail())
                .ifPresentOrElse(a -> {
                    throw new IllegalArgumentException();
                }, () -> repository.insert(account));

    }

    public Optional<Account> getAccountsByMail(String mail) {
        return repository.findAccountsByMail(mail);
    }

    public Optional<Account> getAccountsByName(String name) {
        return repository.findAccountsByName(name);
    }
}
