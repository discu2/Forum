package org.discu2.forum.account.util;

import com.auth0.jwt.JWT;
import org.discu2.forum.account.config.TokenConfig;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.account.service.AccountService;
import org.discu2.forum.common.util.AbstractSpringContext;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class TokenFactory {

    private static final TokenConfig tokenConfig = AbstractSpringContext.getBean(TokenConfig.class);
    private static final AccountService ACCOUNT_SERVICE = AbstractSpringContext.getBean(AccountService.class);

    private static int ACCESS_TOKEN_EXPIRES_TIME_MILLIS = tokenConfig.getAccess_expires_time() * 1000;
    //public static final Algorithm ALGORITHM = ;

    public static String createAccessToken(Account account, HttpServletRequest request) {

        var ip = request.getRemoteAddr();

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("username", account.getUsername())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("ip", ip)
                .sign(tokenConfig.getAlgorithm());

    }

    public static String createRefreshToken(Account account, HttpServletRequest request) {

        var uuid = UUID.randomUUID().toString();

        var token = JWT.create()
                .withSubject(account.getUsername())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("username", account.getUsername())
                .withClaim("uuid", uuid)
                .sign(tokenConfig.getAlgorithm());

        ACCOUNT_SERVICE.addNewRefreshTokenUUID(account, uuid);

        return token;
    }
}
