package com.uc.ms_security.service;

import com.uc.ms_security.config.JwtConfig;
import com.uc.ms_security.dto.auth.AuthResponseDTO;
import com.uc.ms_security.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTests {

    private static final String TEST_SECRET =
            "test-only-jwt-secret-with-at-least-32-bytes";

    @Test
    void generatesTokenAcceptedByDecoderWithExpectedClaims() {
        SecretKey key = new SecretKeySpec(
                TEST_SECRET.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
        JwtConfig config = new JwtConfig();
        JwtEncoder encoder = config.jwtEncoder(key);
        JwtDecoder decoder = config.jwtDecoder(key);
        JwtService jwtService = new JwtService(encoder);
        User user = new User();
        user.setId(42L);
        user.setEmail("ana@example.com");

        AuthResponseDTO response = jwtService.generateToken(user);
        Jwt decoded = decoder.decode(response.accessToken());

        assertEquals("Bearer", response.tokenType());
        assertEquals("42", decoded.getSubject());
        assertEquals("ana@example.com", decoded.getClaimAsString("email"));
        assertEquals(JwtConfig.ISSUER, decoded.getClaimAsString("iss"));
        assertEquals(
                response.expiresAt().truncatedTo(ChronoUnit.SECONDS),
                decoded.getExpiresAt()
        );
        assertTrue(decoded.getId() != null && !decoded.getId().isBlank());
    }

    @Test
    void decoderRejectsTokenWithModifiedSignature() {
        SecretKey key = new SecretKeySpec(
                TEST_SECRET.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
        JwtConfig config = new JwtConfig();
        JwtService jwtService = new JwtService(config.jwtEncoder(key));
        JwtDecoder decoder = config.jwtDecoder(key);
        User user = new User();
        user.setId(42L);
        user.setEmail("ana@example.com");

        String[] parts = jwtService.generateToken(user).accessToken().split("\\.");
        char replacement = parts[2].charAt(0) == 'A' ? 'B' : 'A';
        parts[2] = replacement + parts[2].substring(1);
        String tamperedToken = String.join(".", parts);

        assertThrows(JwtException.class, () -> decoder.decode(tamperedToken));
    }
}
