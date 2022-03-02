package org.discu2.forum.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discu2.forum.model.Account;
import org.discu2.forum.packet.RegisterRequestPacket;
import org.discu2.forum.packet.TokenPacket;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.util.JsonConverter;
import org.discu2.forum.util.TokenFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/register", produces = "application/json")
    public void registerAccount(HttpServletRequest request) throws IOException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), RegisterRequestPacket.class);

        accountService.registerNewAccount(packet.getUsername(), packet.getPassword(), null, packet.getMail(), null);

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

//    @PreAuthorize("(authentication.name == #username) and hasAuthority('account_self')")
//    @PutMapping(value = "/{username}/edit", produces = "application/json")
//    public void editAccount(@PathVariable("username") String username, HttpServletRequest request)
//            throws UsernameNotFoundException, IOException {
//
//        var packet = JsonConverter.requestToPacket(request.getInputStream(), AccountUpdateRequestPacket.class);
//        var account = ((Account.UserDetailImpl)accountService.loadUserByUsername(username)).account;
//
//        account.setNickname(packet.getNickname());
//        accountRepository.save(account);
//
//    }

}
