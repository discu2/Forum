package org.discu2.forum.service;

import lombok.AllArgsConstructor;
import org.discu2.forum.model.LoginToken;
import org.discu2.forum.repository.LoginTokenRepository;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class LoginTokenService implements PersistentTokenRepository {

    private final LoginTokenRepository repository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        var loginToken = new LoginToken(null, token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());

        repository.save(loginToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {

        var token = repository.findBySeries(series);

        token.ifPresent(t -> repository.save(new LoginToken(t.getId(), t.getUsername(), series, tokenValue, lastUsed)));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {

        var loginToken = repository.findBySeries(seriesId);

        if (loginToken.isPresent()) {
            var t = loginToken.get();
            return new PersistentRememberMeToken(t.getUsername(), t.getSeries(), t.getTokenValue(), t.getDate());
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username) {

        var loginToken = repository.findByUsername(username);

        loginToken.ifPresent(t -> repository.delete(t));
    }
}
