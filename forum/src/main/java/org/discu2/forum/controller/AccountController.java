package org.discu2.forum.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discu2.forum.api.exception.BadPacketFormatException;
import org.discu2.forum.model.Account;
import org.discu2.forum.api.packet.AccountPacket;
import org.discu2.forum.api.packet.AccountUpdateRequestPacket;
import org.discu2.forum.api.packet.RegisterRequestPacket;
import org.discu2.forum.api.packet.TokenPacket;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.util.JsonConverter;
import org.discu2.forum.util.TokenFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/register", produces = "application/json")
    public void registerAccount(HttpServletRequest request) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), RegisterRequestPacket.class);

        try {
            accountService.registerNewAccount(packet.getUsername(), packet.getPassword(), null, packet.getMail(), null);
        } catch (NullPointerException e) {
            throw new BadPacketFormatException(e.getMessage());
        }

    }

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws
            JWTDecodeException, UsernameNotFoundException, IOException {

        var authorizationHeader = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var refreshToken = authorizationHeader.substring("Bearer ".length());
        var algorithm = TokenFactory.ALGORITHM;
        var verifier = JWT.require(algorithm).build();
        var decodedJWT = verifier.verify(refreshToken);
        var username = decodedJWT.getSubject();
        var uuid = decodedJWT.getClaim("uuid").asString();
        var account = accountService.loadUserByUsername(username);

        if (!accountService.isRefreshTokenUUIDValid(account, uuid)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        var accessToken = TokenFactory.createAccessToken((Account) account, request);
        var packet = new TokenPacket(accessToken, refreshToken);

        JsonConverter.PacketToJsonResponse(response, packet);

    }

    @GetMapping("/{nameOrId}")
    public void getAccount(@PathVariable String nameOrId,
                           @RequestParam(required = false, defaultValue = "name") String type,
                           HttpServletRequest request, HttpServletResponse response) throws UsernameNotFoundException, IOException {

        AccountPacket packet = null;

        if (type.equals("name")) {
            packet = new AccountPacket((Account) accountService.loadUserByUsername(nameOrId));
        }

        if (type.equals("id")) {
            packet = new AccountPacket(accountService.loadUserById(nameOrId));
        }

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
    @PostMapping(value = "/{username}/profile_pic", produces = "multipart/form-data")
    public void uploadProfilePicture(@PathVariable String username, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        accountService.addProfilePicture(username, request.getPart("Image"));
    }

    @GetMapping(value = "/{username}/profile_pic")
    public void getProfilePicture(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) throws IOException {

        accountService.loadProfilePicture(username, response);
    }

}
