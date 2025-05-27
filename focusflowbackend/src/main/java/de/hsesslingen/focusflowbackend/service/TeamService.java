package de.hsesslingen.focusflowbackend.service;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling team-related operations
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // Method: Create a new team with a name, description and members
    public Team createTeam(String name, String description, List<String> memberEmails, String creatorEmail) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name is required");
        }

        Team team = new Team();
        team.setName(name);
        team.setDescription(description);

        Set<User> members = new HashSet<>();

        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("Creator not found: " + creatorEmail));
        members.add(creator);

        if (memberEmails != null) {
            for (String email : memberEmails) {
                User member = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found: " + email));
                members.add(member);
            }
        }

        team.setMembers(members);
        return teamRepository.save(team);
    }

    // Method: Get all teams for a specific user
    public List<Team> getTeamsForUser(Long userId) {
        return teamRepository.findAll().stream()
                .filter(team -> team.getMembers().stream().anyMatch(user -> user.getId().equals(userId)))
                .collect(Collectors.toList());
    }

    // Method: Get a team by its ID
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    // Method: Get all teams
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
