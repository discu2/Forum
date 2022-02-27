package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.exception.AccountAlreadyExistException;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public void registerNewAccount(Account account) throws AccountAlreadyExistException {

        try {
            accountRepository.save(account);
        } catch (Exception e) {
            throw new AccountAlreadyExistException();
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var accountByMail = getAccountByMail(username);
        var accountByName = getAccountByName(username);

        if (accountByMail.isPresent()) return accountByMail.get().getUserDetails();
        if (accountByName.isPresent()) return accountByName.get().getUserDetails();

        throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }

    private Optional<Account> getAccountByMail(String mail) {
        return accountRepository.findAccountByMail(mail);
    }

    private Optional<Account> getAccountByName(String name) {
        return accountRepository.findAccountByUsername(name);
    }

}
