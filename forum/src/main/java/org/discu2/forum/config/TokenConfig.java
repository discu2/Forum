package org.discu2.forum.config;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Component
@Data
@ConfigurationProperties(prefix = "forum.token")
@Validated
public class TokenConfig {

    @NotNull
    @Min(10)
    @Max(Integer.MAX_VALUE)
    private int access_expires_time;

    @NotNull
    @Length(min = 32)
    private String crypto_key;
}
