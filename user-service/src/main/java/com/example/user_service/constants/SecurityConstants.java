package com.example.user_service.constants;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final int STRENGTH = 14;
    public static final BCryptVersion B_CRYPT_VERSION = BCryptVersion.$2B;
}
