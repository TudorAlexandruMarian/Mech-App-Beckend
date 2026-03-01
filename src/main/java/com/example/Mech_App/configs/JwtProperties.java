package com.example.Mech_App.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "mech-app-secret-key-change-in-production-min-256-bits";
    private long accessValidityMs = 900_000;   // 15 min
    private long refreshValidityMs = 604_800_000;  // 7 days

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessValidityMs() {
        return accessValidityMs;
    }

    public void setAccessValidityMs(long accessValidityMs) {
        this.accessValidityMs = accessValidityMs;
    }

    public long getRefreshValidityMs() {
        return refreshValidityMs;
    }

    public void setRefreshValidityMs(long refreshValidityMs) {
        this.refreshValidityMs = refreshValidityMs;
    }
}
