package de.hsesslingen.focusflowbackend.service;

import org.springframework.stereotype.Service;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling user-related operations
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Method: Register a user with valid credentials
    public User registerUser(User user) {
        // Check if password confirmation matches
        if (user.getPasswordConfirm() == null || !user.getPassword().equals(user.getPasswordConfirm())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        // Validate user credentials
        if (!isValid(user)) {
            throw new IllegalArgumentException("Invalid user data");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Method: Validate credentials for login (check if email exists and password matches)
    public boolean loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;
    }

    // Method: Validate user credentials (password strength and email format)
    private boolean isValid(User user) {
        return user.getPassword() != null && isPasswordValid(user.getPassword()) 
            && user.getEmail() != null && user.getEmail().contains("@");
    }

    // Password validation with required rules (length, uppercase, lowercase, special character)
    private boolean isPasswordValid(String password) {
        if (password.length() < 10 || password.length() > 12) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasSpecialChar;
    }

    // Method: Assign a role to a user, ensure the role is valid
    public User assignRole(User user, String role) {
        user.setRole(role);
        return userRepository.save(user);
    }

    // Method: Add a user to a team
    public void addUserToTeam(User user, Team team) {
        Set<Team> userTeams = user.getTeams();
        userTeams.add(team);
        user.setTeams(userTeams); // Ensure the set is updated

        Set<User> teamMembers = team.getMembers();
        teamMembers.add(user);
        team.setMembers(teamMembers); // Ensure the team set is updated

        userRepository.save(user);
    }

    // Method: Remove a user from a team
    public void removeUserFromTeam(User user, Team team) {
        Set<Team> userTeams = user.getTeams();
        userTeams.remove(team);
        user.setTeams(userTeams); // Ensure the set is updated

        Set<User> teamMembers = team.getMembers();
        teamMembers.remove(user);
        team.setMembers(teamMembers); // Ensure the team set is updated

        userRepository.save(user);
    }
}
