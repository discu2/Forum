package org.discu2.forum.account.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.discu2.forum.account.config.TokenConfig;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.account.service.TokenService;
import org.discu2.forum.common.util.JsonConverter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class AuthController {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final TokenConfig tokenConfig;

    @GetMapping("/refresh_token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws
            JWTDecodeException, UsernameNotFoundException, IOException {

        var authorizationHeader = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var refreshToken = authorizationHeader.substring("Bearer ".length());
        var verifier = JWT.require(tokenConfig.getAlgorithm()).build();
        var decodedJWT = verifier.verify(refreshToken);
        var username = decodedJWT.getSubject();
        var uuid = decodedJWT.getClaim("uuid").asString();
        var account = accountService.loadUserByUsername(username);

        if (!accountService.isRefreshTokenUUIDValid(account, uuid)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        var packet = tokenService.createTokenDto((Account) account, request, refreshToken);

        JsonConverter.PacketToJsonResponse(response, packet);

    }
}
