package org.discu2.forum.controller;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.discu2.forum.model.Account;
import org.discu2.forum.packet.ErrorMessagePacket;
import org.discu2.forum.packet.RegisterRequestPacket;
import org.discu2.forum.packet.TokenPacket;
import org.discu2.forum.repository.RoleRepository;
import org.discu2.forum.service.AccountService;
import org.discu2.forum.util.TokenFactory;
import org.springframework.http.ResponseEntity;
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
    private static final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var packet =  mapper.readValue(request.getInputStream(), RegisterRequestPacket.class);

        var account = new Account(
                null,
                packet.getUsername(),
                passwordEncoder.encode(packet.getPassword()),
                roleRepository.findRoleByName("DEFAULT").get().getGrantedAuthorities(),
                true,
                true,
                true,
                true,
                packet.getMail(),
                false
        );

        try {
            service.registerNewAccount(account);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            mapper.writeValue(response.getOutputStream(), new ErrorMessagePacket("Account already exist"));
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                var refreshToken = authorizationHeader.substring("Bearer ".length());
                var algorithm = TokenFactory.ALGORITHM;
                var verifier = JWT.require(algorithm).build();
                var decodedJWT = verifier.verify(refreshToken);
                var username = decodedJWT.getSubject();
                var account = service.getAccountByName(username).get();
                var accessToken = TokenFactory.createAccessToken(account, request);
                var packet = new TokenPacket(accessToken, refreshToken);

                response.setContentType(APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getOutputStream(), packet);

                return ResponseEntity.ok().build();

            } catch (Exception e) {
                mapper.writeValue(response.getOutputStream(), new ErrorMessagePacket(e.getMessage()));
                return ResponseEntity.badRequest().build();
            }

        }

        mapper.writeValue(response.getOutputStream(), new ErrorMessagePacket("Bad format"));
        return ResponseEntity.badRequest().build();

    }

}
