package de.hsesslingen.focusflowbackend;

import de.hsesslingen.focusflowbackend.repository.UserRepository;
import de.hsesslingen.focusflowbackend.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save and retrieve user by ID")
    void testSaveAndFindById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("pass12345");
        user.setRole("USER");

        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Find user by email")
    void testFindByEmail() {
        User user = new User();
        user.setEmail("email@example.com");
        user.setPassword("password");
        user.setRole("USER");

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("email@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("email@example.com");
    }

    @Test
    @DisplayName("Find users by role")
    void testFindByRole() {
        User user1 = new User();
        user1.setEmail("admin@example.com");
        user1.setPassword("adminpass");
        user1.setRole("ADMIN");

        User user2 = new User();
        user2.setEmail("user@example.com");
        user2.setPassword("userpass");
        user2.setRole("USER");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> admins = userRepository.findByRole("ADMIN");
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("Delete user by ID")
    void testDeleteById() {
        User user = new User();
        user.setEmail("delete@example.com");
        user.setPassword("pass");
        user.setRole("USER");

        User saved = userRepository.save(user);
        userRepository.deleteById(saved.getId());

        Optional<User> deleted = userRepository.findById(saved.getId());
        assertThat(deleted).isNotPresent();
    }
}

