package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewAccount(Account account) {

        repository.findAccountByMail(account.getMail())
                .ifPresentOrElse(a -> {
                    throw new IllegalArgumentException();
                }, () -> repository.insert(account));

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var accountByMail = getAccountByMail(username);
        var accountByName = getAccountByName(username);

        if (accountByMail.isPresent()) return accountByMail.get();
        if (accountByName.isPresent()) return accountByName.get();

        throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }

    public Optional<Account> getAccountByMail(String mail) {
        return repository.findAccountByMail(mail);
    }

    public Optional<Account> getAccountByName(String name) {
        return repository.findAccountByUsername(name);
    }

}
