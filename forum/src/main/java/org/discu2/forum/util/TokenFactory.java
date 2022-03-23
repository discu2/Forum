package org.discu2.forum.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.discu2.forum.config.TokenConfig;
import org.discu2.forum.model.Account;
import org.discu2.forum.service.AccountService;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class TokenFactory {

    private static final TokenConfig tokenConfig = SpringContext.getBean(TokenConfig.class);
    private static final AccountService ACCOUNT_SERVICE = SpringContext.getBean(AccountService.class);

    private static int ACCESS_TOKEN_EXPIRES_TIME_MILLIS = tokenConfig.getAccess_expires_time() * 1000;
    public static final Algorithm ALGORITHM = Algorithm.HMAC512(tokenConfig.getCrypto_key().getBytes());

    public static String createAccessToken(Account account, HttpServletRequest request) {

        var ip = request.getRemoteAddr();

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("user_id", account.getId())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("ip", ip)
                .sign(ALGORITHM);

    }

    public static String createRefreshToken(Account account, HttpServletRequest request) {

        var uuid = UUID.randomUUID().toString();

        var token = JWT.create()
                .withSubject(account.getUsername())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("id", account.getId())
                .withClaim("uuid", uuid)
                .sign(ALGORITHM);

        ACCOUNT_SERVICE.addNewRefreshTokenUUID(account, uuid);

        return token;
    }
}