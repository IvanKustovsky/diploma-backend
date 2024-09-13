package com.e2rent.identity_service.utils;

import com.e2rent.identity_service.config.JwtProperties;
import com.e2rent.identity_service.exception.JwtGenerationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private final JwtDecoder jwtDecoder;

    public boolean isTokenValid(String token) {
        Jwt decodedJwt = jwtDecoder.decode(token);
        return hasExpectedRole(decodedJwt) && hasExpectedSubject(decodedJwt);
    }

    public String generateToken(String email, Collection<String> roles) {
        var key = jwtProperties.getKey();
        var algorithm = jwtProperties.getAlgorithm();

        var header = new JWSHeader(algorithm);
        var claimsSet = buildClaimsSet(email, roles);

        var jwt = new SignedJWT(header, claimsSet);
        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        } catch (JOSEException exception) {
            throw new JwtGenerationException("Unable to generate JWT");
        }

        return jwt.serialize();
    }

    public Jwt parseToken(String token) {
        return jwtDecoder.decode(token);
    }

    private boolean hasExpectedRole(Jwt jwt) {
        var roles = jwt.getClaimAsStringList("role");
        return roles != null && roles.contains("USER");
    }

    private boolean hasExpectedSubject(Jwt jwt) {
        var subject = jwt.getSubject();

        String emailPattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailPattern);

        return subject != null && pattern.matcher(subject).matches();
    }

    private JWTClaimsSet buildClaimsSet(String email, Collection<String> roles) {
        var issuer = jwtProperties.getIssuer();
        var issuedAt = Instant.now();
        var expirationTime = issuedAt.plus(jwtProperties.getExpiresIn());

        return new JWTClaimsSet.Builder()
                .subject(email)
                .claim("role", roles)
                .issuer(issuer)
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expirationTime))
                .build();
    }
}
