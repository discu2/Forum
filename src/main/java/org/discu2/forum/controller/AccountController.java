package org.discu2.forum.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.discu2.forum.exception.AccountAlreadyExistException;
import org.discu2.forum.exception.BadPacketFormatException;
import org.discu2.forum.model.Account;
import org.discu2.forum.packet.RegisterRequestPacket;
import org.discu2.forum.packet.TokenPacket;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.util.JsonConverter;
import org.discu2.forum.util.TokenFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final AccountService service;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    public void registerAccount(HttpServletRequest request) throws IOException, AccountAlreadyExistException, BadPacketFormatException {

        var packet = JsonConverter.requestToPacket(request.getInputStream(), RegisterRequestPacket.class);

        var defaultRole = roleRepository.findRoleByName("DEFAULT").orElseThrow(RuntimeException::new);

        var account = new Account(
                null,
                packet.getUsername(),
                passwordEncoder.encode(packet.getPassword()),
                defaultRole.getGrantedAuthorities(),
                true,
                true,
                true,
                true,
                packet.getMail(),
                false
        );

        service.registerNewAccount(account);

    }

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JWTDecodeException, IOException {
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
            var account = service.loadUserByUsername(username);
            var accessToken = TokenFactory.createAccessToken(account, request);
            var packet = new TokenPacket(accessToken, refreshToken);

            response.setContentType(APPLICATION_JSON_VALUE);
            JsonConverter.PacketToJsonResponse(response.getOutputStream(), packet);

    }

}
