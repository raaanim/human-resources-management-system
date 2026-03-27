package aitho.ranim.hrms.dto;

public record ActivateAccountRequest(
        String token,
        String newPassword
) {
}
