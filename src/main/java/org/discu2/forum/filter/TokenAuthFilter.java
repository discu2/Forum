package org.discu2.forum.filter;

import com.auth0.jwt.JWT;
import com.google.common.base.Strings;
import org.discu2.forum.packet.ErrorMessagePacket;
import org.discu2.forum.util.JsonConverter;
import org.discu2.forum.util.TokenFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TokenAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/account/refresh_token")
                || request.getServletPath().startsWith("/account/login")
                || request.getServletPath().startsWith("/account/register")) {

            filterChain.doFilter(request, response);
            return;
        }

        var authorizationHeader = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            var token = authorizationHeader.substring("Bearer ".length());
            var algorithm = TokenFactory.ALGORITHM;
            var verifier = JWT.require(algorithm).build();
            var decodedJWT = verifier.verify(token);
            var username = decodedJWT.getSubject();
            var roles = decodedJWT.getClaim("roles").asList(String.class);
            var ip = decodedJWT.getClaim("ip").asString();

            if (!request.getRemoteAddr().equals(ip))
                throw new RuntimeException("Client ip does not match with token.");

            var authorities = new ArrayList<SimpleGrantedAuthority>();
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            JsonConverter.PacketToJsonResponse(response, new ErrorMessagePacket(HttpStatus.FORBIDDEN, e.getMessage()));
        }

    }

}
