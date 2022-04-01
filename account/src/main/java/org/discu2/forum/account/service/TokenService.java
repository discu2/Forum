package org.discu2.forum.account.service;

import com.auth0.jwt.JWT;
import lombok.NonNull;
import org.discu2.forum.account.config.TokenConfig;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.common.packet.TokenPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final TokenConfig tokenConfig;
    private final AccountService accountService ;
    private int accessTokenExpiresTimeMillis;

    @Autowired
    public TokenService(TokenConfig tokenConfig, AccountService accountService) {
        this.tokenConfig = tokenConfig;
        this.accountService = accountService;
        this.accessTokenExpiresTimeMillis = tokenConfig.getAccess_expires_time() * 1000;
    }

    public TokenPacket createTokenDto(@NonNull Account account, @NonNull HttpServletRequest request) {
        return createTokenDto(account, request, null);
    }

    public TokenPacket createTokenDto(@NonNull Account account, @NonNull HttpServletRequest request, String refreshToken) {

        var expireTime = new Date(System.currentTimeMillis() + accessTokenExpiresTimeMillis);
        if (refreshToken == null) refreshToken = createRefreshToken(account, request);
        var accessToken = createAccessToken(account, request, expireTime);

        return new TokenPacket(accessToken, accessTokenExpiresTimeMillis, expireTime.getTime(), refreshToken);
    }
    private String createAccessToken(Account account, HttpServletRequest request, Date expireTime) {

        var ip = request.getRemoteAddr();

        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(expireTime)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("username", account.getUsername())
                .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("ip", ip)
                .sign(tokenConfig.getAlgorithm());

    }

    private String createRefreshToken(Account account, HttpServletRequest request) {

        var uuid = UUID.randomUUID().toString();

        var token = JWT.create()
                .withSubject(account.getUsername())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("username", account.getUsername())
                .withClaim("uuid", uuid)
                .sign(tokenConfig.getAlgorithm());

        accountService.addNewRefreshTokenUUID(account, uuid);

        return token;
    }
}
