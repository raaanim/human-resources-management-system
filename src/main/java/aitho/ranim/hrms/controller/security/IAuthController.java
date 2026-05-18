package aitho.ranim.hrms.controller.security;

import aitho.ranim.hrms.dto.security.LoginRequest;
import aitho.ranim.hrms.dto.security.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAuthController {

    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);
}
