package org.discu2.forum.account.controller;

import lombok.AllArgsConstructor;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.common.exception.BadPacketFormatException;
import org.discu2.forum.common.packet.AccountPacket;
import org.discu2.forum.common.packet.AccountUpdateRequestPacket;
import org.discu2.forum.common.packet.RegisterRequestPacket;
import org.discu2.forum.common.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/register")
    public void registerAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), RegisterRequestPacket.class);

        try {
            accountService.registerNewAccount(packet.getUsername(), packet.getPassword(), null, packet.getMail(), null);
        } catch (NullPointerException e) {
            throw new BadPacketFormatException(e.getMessage());
        }

    }

    @GetMapping("/{username}")
    public void getAccount(@PathVariable String username, HttpServletResponse response) throws UsernameNotFoundException, IOException {

        var account = (Account)accountService.loadUserByUsername(username);
        var packet = new AccountPacket(account.getUsername(), account.getNickname(), account.getRoleIds(), account.getAvatarIds());


        if (packet != null) JsonConverter.PacketToJsonResponse(response, packet);

    }

    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    @PutMapping(value = "/{username}", produces = "application/json")
    public void editAccount(@PathVariable("username") String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException, UsernameNotFoundException, IllegalArgumentException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), AccountUpdateRequestPacket.class);

        accountService.editAccount(username, packet);
    }

}
