package team14.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Value("${security.jwt-token.secret}")
    private String secret;

    @Value("${security.jwt-token.expires}")
    private Long expires;

    @Value("${security.jwt-app-name}")
    private String appName;

    @Bean
    public String jwtSecret() {
        return secret;
    }

    @Bean
    public Long jwtExpiresIn() {
        return expires;
    }

    @Bean
    public String appName() { return appName;}
}
