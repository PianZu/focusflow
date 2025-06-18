package de.hsesslingen.focusflowbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import de.hsesslingen.focusflowbackend.model.tasks.Task;

@Entity
@Data
@EqualsAndHashCode(exclude = {"members","tasks"})
@Table(name = "teams")
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Team → User (Owning Side)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
      name = "team_members",
      joinColumns = @JoinColumn(name = "team_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference("team-members")
    private Set<User> members = new HashSet<>();

    // Team → Task (Inverse Side)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("team-tasks")
    private Set<Task> tasks = new HashSet<>();
}
