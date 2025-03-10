package ru.dvume.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.dvume.consts.AuthType;

@Data
@Component
@ConfigurationProperties("auth")
public class AuthProperties {
    private AuthType authType = AuthType.BASIC;
    private UserCreds user;
    private UserCreds admin;

    @Data
    public static class UserCreds {
        private String name;
        private String pwd;
    }
}
