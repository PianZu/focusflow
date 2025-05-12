package de.hsesslingen.focusflowbackend.dto;

import lombok.Data;

@Data
/**
 * UserRegistrationRequestDTO is a Data Transfer Object (DTO) for user registration.
 * It contains the necessary fields for a user to register in the system.
 */
public class UserRegistrationRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirm;
}

