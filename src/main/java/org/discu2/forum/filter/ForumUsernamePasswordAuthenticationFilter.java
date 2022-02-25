package org.discu2.forum.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.packet.ErrorMessagePacket;
import org.discu2.forum.packet.LoginRequestPacket;
import org.discu2.forum.packet.TokenPacket;
import org.discu2.forum.util.TokenFactory;
import org.springframework.security.authentication.AuthenticationManager;
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

@AllArgsConstructor
public class ForumUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        var authToken = new UsernamePasswordAuthenticationToken("", "");

        try {
            var account = mapper.readValue(request.getInputStream(), LoginRequestPacket.class);
            authToken = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var msg = new ErrorMessagePacket(e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);

            try {
                mapper.writeValue(response.getOutputStream(), msg);
            } catch (IOException ex) { }
        }

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        var account = (Account)authResult.getPrincipal();
        var accessToken = TokenFactory.createAccessToken(account, request);
        var refreshToken = TokenFactory.createRefreshToken(account, request);

        var packet = new TokenPacket(accessToken, refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), packet);
    }
}
