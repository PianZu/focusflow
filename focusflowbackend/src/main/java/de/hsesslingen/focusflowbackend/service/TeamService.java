package de.hsesslingen.focusflowbackend.service;

import org.springframework.stereotype.Service;
import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling team-related operations
public class TeamService {

    private TeamRepository teamRepository;
    private UserRepository userRepository;

    // Method: Save the team to the database
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    // Method: Find team by ID
    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

    // Method: Add a user to a team
    public void addMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found, cannot add member"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found, cannot add to team"));

        team.getMembers().add(user);
        user.getTeams().add(team);
        teamRepository.save(team);
    }
    
    // Method: Remove a user from a team
    public void removeMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found, cannot remove member"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found, cannot remove from team"));

        team.getMembers().remove(user);
        user.getTeams().remove(team);
        teamRepository.save(team);
    }
}
