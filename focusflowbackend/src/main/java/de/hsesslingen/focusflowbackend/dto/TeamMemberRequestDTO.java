// src/main/java/de/hsesslingen/focusflowbackend/dto/TeamMemberRequestDTO.java
package de.hsesslingen.focusflowbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamMemberRequestDTO {
    private List<String> memberEmails;
}

