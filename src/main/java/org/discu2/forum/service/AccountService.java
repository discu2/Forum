package org.discu2.forum.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.discu2.forum.exception.AlreadyExistException;
import org.discu2.forum.exception.DataNotFoundException;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.discu2.forum.model.Account.MAX_REFRESH_TOKENS;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    /**
     *
     * @param username
     * @param password
     * @param roleId - Leave this null or empty String to use DEFAULT role
     * @param mail
     * @param nickname - Leave this null or empty String to use username for nickname
     * @return
     * @throws AlreadyExistException
     */
    public Account registerNewAccount(@NonNull String username, @NonNull String password, String roleId, @NonNull String mail, String nickname)
            throws AlreadyExistException, DataNotFoundException {

        var account = new Account(
                null,
                username,
                passwordEncoder.encode(password),
                Strings.isNullOrEmpty(roleId) ? Sets.newHashSet(roleService.loadRoleByName("DEFAULT").getId()) : Sets.newHashSet(roleId),
                true,
                true,
                true,
                true,
                mail,
                false,
                Lists.newArrayList(),
                Strings.isNullOrEmpty(nickname) ? username : nickname
        );

        try {
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new AlreadyExistException(Account.class);
        }

    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        var accountByMail = getAccountByMail(username);
        var accountByName = getAccountByName(username);

        if (accountByMail.isPresent()) return accountByMail.get();
        if (accountByName.isPresent()) return accountByName.get();

        throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }

    public Boolean isRefreshTokenUUIDValid(@NonNull UserDetails account, String uuid) {

        if (Strings.isNullOrEmpty(uuid)) return false;

        if (!((Account) account).getRefreshTokenUUIDs().contains(uuid)) return false;

        return true;
    }

    public void addNewRefreshTokenUUID(@NonNull Account account, @NonNull String uuid) {

        var uuids = account.getRefreshTokenUUIDs();

        if (uuids.size() >= MAX_REFRESH_TOKENS) uuids.remove(0);
        uuids.add(uuid);

        accountRepository.save(account);
    }

    private Optional<Account> getAccountByMail(@NonNull String mail) {
        return accountRepository.findAccountByMail(mail);
    }

    private Optional<Account> getAccountByName(@NonNull String name) {
        return accountRepository.findAccountByUsername(name);
    }

}
