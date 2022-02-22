package org.discu2.forum.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.discu2.forum.model.Account;
import org.discu2.forum.repository.AccountRepository;
import org.discu2.forum.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("pw") String pw, Map<String,Object> map) {

        if (checkLogin(name, pw)) return "/index";

        map.put("msg", "Wrong password");
        return "";
    }

    @PostMapping("/register")
    public String registerAccount(@RequestParam("mail") String mail, @RequestParam("name") String name, @RequestParam("pw") String pw, Map<String, Object> map) {

        var account = new Account(UUID.randomUUID(), mail, name, DigestUtils.sha256Hex(pw), false);

        try {
            service.registerNewAccount(account);
            return "";
        } catch (Exception e) {
            map.put("error", "Account already exist");
            return "";
        }
    }

    public boolean checkLogin(String name, String pw) {

        var accountByMail = service.getAccountsByMail(name);
        var accountByName = service.getAccountsByName(name);
        var pwHash = DigestUtils.sha256Hex(pw);

        if ((accountByMail.isPresent() && pwHash.equals(accountByMail.get().getPassWordHash()))
                || (accountByName.isPresent() && pwHash.equals(accountByName.get().getPassWordHash()))) return true;

        return false;
    }
}
