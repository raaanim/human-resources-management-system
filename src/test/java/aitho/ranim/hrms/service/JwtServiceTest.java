package aitho.ranim.hrms.service;


import aitho.ranim.hrms.service.security.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;


public class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret",
                "testsecretkeytestsecretkeytestsecretkey");

        ReflectionTestUtils.setField(jwtService, "expirationMs",
                86400000L);

        userDetails = User
                .withUsername("testUser")
                .build();
    }

    @Test
    public void givenValidUserDetails_whenGenerateToken_thenTokenIsNotNullAndNotEmpty(){;

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void givenGeneratedToken_whenExtractUsername_thenReturnsOriginalUsername() {

        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    public void givenGeneratedToken_whenValidated_thenIsValid() {

        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }
}

