package org.discu2.forum.account.filter;

import lombok.RequiredArgsConstructor;
import org.discu2.forum.account.model.Account;
import org.discu2.forum.account.service.TokenService;
import org.discu2.forum.account.util.SpringContext;
import org.discu2.forum.common.packet.ErrorMessagePacket;
import org.discu2.forum.common.packet.LoginRequestPacket;
import org.discu2.forum.common.util.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class ForumUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService = SpringContext.getBean(TokenService.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        var authToken = new UsernamePasswordAuthenticationToken("", "");

        try {
            var account = JsonConverter.requestToPacket(request.getInputStream(), LoginRequestPacket.class);
            authToken = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());

            super.setDetails(request, authToken);

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var msg = new ErrorMessagePacket(HttpStatus.BAD_REQUEST, e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);

            try {
                JsonConverter.PacketToJsonResponse(response, msg);
            } catch (IOException ex) { }
        }

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        var account = (Account)authResult.getPrincipal();
        var packet = tokenService.createTokenDto(account, request);

        JsonConverter.PacketToJsonResponse(response, packet);
    }
}
