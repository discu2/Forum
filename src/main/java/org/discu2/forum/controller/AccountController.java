package org.discu2.forum.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.discu2.forum.exception.AccountAlreadyExistException;
import org.discu2.forum.exception.BadPacketFormatException;
import org.discu2.forum.model.Account;
import org.discu2.forum.packet.AccountUpdateRequestPacket;
import org.discu2.forum.packet.RegisterRequestPacket;
import org.discu2.forum.packet.TokenPacket;
import org.discu2.forum.repository.AccountRepository;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.util.JsonConverter;
import org.discu2.forum.util.TokenFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public void registerAccount(HttpServletRequest request) throws IOException, AccountAlreadyExistException, BadPacketFormatException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), RegisterRequestPacket.class);

        var defaultRole = roleRepository.findByName("DEFAULT").orElseThrow(RuntimeException::new);

        var account = new Account(
                null,
                packet.getUsername(),
                passwordEncoder.encode(packet.getPassword()),
                Sets.newHashSet(defaultRole.getId()),
                true,
                true,
                true,
                true,
                packet.getMail(),
                false,
                packet.getUsername()
        );

        accountService.registerNewAccount(account);

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
            var account = accountService.loadUserByUsername(username);
            var accessToken = TokenFactory.createAccessToken((Account.UserDetailImpl) account, request);
            var packet = new TokenPacket(accessToken, refreshToken);

            response.setContentType(APPLICATION_JSON_VALUE);
            JsonConverter.PacketToJsonResponse(response.getOutputStream(), packet);

    }

}
