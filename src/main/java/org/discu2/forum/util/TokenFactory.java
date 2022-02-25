package org.discu2.forum.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.discu2.forum.model.Account;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenFactory {

    private static final int ACCESS_TOKEN_EXPIRES_TIME_MILLIS = 10*60*1000;
    private static final int REFRESH_TOKEN_EXPIRES_TIME_MILLIS = 2*7*24*60*60*1000;
    public static final Algorithm ALGORITHM = Algorithm.HMAC256("this is not good".getBytes());

    public static String createAccessToken(Account account, HttpServletRequest request) {

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(ALGORITHM);

    }

    public static String createRefreshToken(Account account, HttpServletRequest request) {

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .sign(ALGORITHM);
    }
}