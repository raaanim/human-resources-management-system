package aitho.ranim.hrms.service;


import aitho.ranim.hrms.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;


public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret",
                "testsecretkeytestsecretkeytestsecretkey");

        ReflectionTestUtils.setField(jwtService, "expirationMs",
                86400000L);
    }

    @Test
    public void givenValidUserDetails_whenGenerateToken_thenTokenIsNotNullAndNotEmpty(){
        UserDetails userDetails = User
                .withUsername("testUser")
                .build();

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void givenGeneratedToken_whenExtractUsername_thenReturnsOriginalUsername() {
        UserDetails userDetails = User
                .withUsername("testUser")
                .build();

        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    public void givenGeneratedToken_whenValidated_thenIsValid() {
        UserDetails userDetails = User
                .withUsername("testUser")
                .build();

        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }
}

