package com.example.user_service.constants;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final int STRENGTH = 14;
    public static final BCryptPasswordEncoder.BCryptVersion B_CRYPT_VERSION = BCryptPasswordEncoder.BCryptVersion.$2B;
}
