package org.discu2.forum.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.discu2.forum.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenFactory {

    private static final int ACCESS_TOKEN_EXPIRES_TIME_MILLIS = 10*60*1000;
    private static final int REFRESH_TOKEN_EXPIRES_TIME_MILLIS = 2*7*24*60*60*1000;
    public static final Algorithm ALGORITHM = Algorithm.HMAC512("this is not good".getBytes());

    public static String createAccessToken(UserDetails account, HttpServletRequest request) {

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(ALGORITHM);

    }

    public static String createRefreshToken(UserDetails account, HttpServletRequest request) {

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRES_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .sign(ALGORITHM);
    }
}
