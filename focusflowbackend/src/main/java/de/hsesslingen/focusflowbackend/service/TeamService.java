package de.hsesslingen.focusflowbackend.service;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
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

    // Method: Add members to an existing team by their email addresses
    @Transactional
    public Team addMembers(Long teamId, List<String> memberEmails) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team nicht gefunden: " + teamId));

        for (String email : memberEmails) {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden: " + email));
            // Fügt intern in team.getMembers() ein, Hibernate erzeugt später INSERT in team_members
            team.getMembers().add(user);
        }
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

    
    // Method: Get all members of a team
    public List<Long> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team not found with ID: " + teamId));
        return team.getMembers().stream()
                .map(User::getId)
                .toList();
    }

    // Method: Delete a team by its ID
    @Transactional
    public void deleteTeam(Long teamId) {
    if (!teamRepository.existsById(teamId)) {
        throw new RuntimeException("Team nicht gefunden: " + teamId);
    }
    teamRepository.deleteById(teamId);
}

}
