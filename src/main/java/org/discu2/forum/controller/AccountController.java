package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService service;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    public String registerAccount(@RequestParam("mail") String mail, @RequestParam("name") String name, @RequestParam("pw") String pw, Map<String, Object> map) {

        var account = new Account(
                name,
                passwordEncoder.encode(pw),
                roleRepository.findRoleByName("DEFAULT").get().getGrantedAuthorities(),
                true,
                true,
                true,
                true,
                mail,
                false
        );

        try {
            service.registerNewAccount(account);
            return "";
        } catch (Exception e) {
            map.put("error", "Account already exist");
            return "";
        }
    }

}
