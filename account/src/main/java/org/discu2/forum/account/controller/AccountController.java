package org.discu2.forum.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.api.exception.DataNotFoundException;
import org.discu2.forum.api.packet.AccountPacket;
import org.discu2.forum.api.packet.AccountUpdateRequestPacket;
import org.discu2.forum.api.packet.RegisterRequestPacket;
import org.discu2.forum.api.util.JsonConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/register")
    public void registerAccount(HttpServletRequest request) throws IOException {

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
        var packet = new AccountPacket(account.getUsername(), account.getNickname(), account.getRoleIds());


        if (packet != null) JsonConverter.PacketToJsonResponse(response, packet);

    }

    @PreAuthorize("authentication.name == #username or hasAuthority('ADMIN')")
    @PutMapping(value = "/{username}", produces = "application/json")
    public void editAccount(@PathVariable("username") String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException, UsernameNotFoundException, IllegalArgumentException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), AccountUpdateRequestPacket.class);

        accountService.editAccount(username, packet);
    }

    @PreAuthorize("authentication.name == #username or hasAuthority('ADMIN')")
    @PostMapping(value = "/{username}/profile_pic")
    public void uploadProfilePicture(@PathVariable String username, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        accountService.addProfilePicture(username,
                Optional.ofNullable(request.getPart("Image"))
                        .orElseThrow(() -> new BadPacketFormatException("Missing \"Image\" key in body")));
    }

    @GetMapping(value = "/{username}/profile_pic")
    public void getProfilePicture(@PathVariable String username, HttpServletResponse response) throws IOException {

        var pic = accountService.loadProfilePictureByUsername(username);

        response.setContentType(pic.getContentType());
        response.getOutputStream().write(pic.getPicture());
    }

}
