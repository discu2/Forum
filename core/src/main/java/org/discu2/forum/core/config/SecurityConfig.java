package org.discu2.forum.core.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import org.discu2.forum.api.filter.TokenAuthFilter;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "forum.auth")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @NotNull
    @Length(min = 32)
    private String crypto_key;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new TokenAuthFilter(Algorithm.HMAC512(crypto_key.getBytes())), UsernamePasswordAuthenticationFilter.class);

    }
}
