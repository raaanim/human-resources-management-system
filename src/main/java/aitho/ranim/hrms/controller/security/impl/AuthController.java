package aitho.ranim.hrms.controller.security.impl;

import aitho.ranim.hrms.controller.security.IAuthController;
import aitho.ranim.hrms.dto.security.LoginRequest;
import aitho.ranim.hrms.dto.security.LoginResponse;
import aitho.ranim.hrms.service.security.IJwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements IAuthController {

    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            IJwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // POST path = http://localhost:8080/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
