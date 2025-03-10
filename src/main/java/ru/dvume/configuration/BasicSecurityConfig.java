package ru.dvume.configuration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.dvume.properties.AuthProperties;

@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "auth", name = "auth-type", havingValue = "BASIC")
public class BasicSecurityConfig {
    private final AuthProperties authProperties;

    public BasicSecurityConfig(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        log.info("--- using BASIC auth");
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/**").hasRole("APPUSER")
                        .requestMatchers("/admin/**").hasRole("APPADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(authProperties.getUser().getName())
                .password(authProperties.getUser().getPwd())
                .roles("APPUSER")
                .build();
        UserDetails admin = User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(authProperties.getAdmin().getName())
                .password(authProperties.getAdmin().getPwd())
                .roles("APPADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
