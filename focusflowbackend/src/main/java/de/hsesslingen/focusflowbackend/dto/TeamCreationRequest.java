package de.hsesslingen.focusflowbackend.dto;

import lombok.Data;
import java.util.List;

@Data
/**
 * TeamCreationRequest is a Data Transfer Object (DTO) for creating a new team.
 * It contains the necessary fields for a user to create a team in the system.
 */
public class TeamCreationRequest {
    private String name;
    private String description;
    private List<String> memberEmails;
    private String creatorEmail;
}
