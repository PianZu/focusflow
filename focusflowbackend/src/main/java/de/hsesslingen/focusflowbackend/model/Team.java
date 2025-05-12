package de.hsesslingen.focusflowbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

@Entity
@Data
@EqualsAndHashCode(exclude = "members")
@Table(name = "teams")
// This class represents a team in the system
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Task> tasks = new HashSet<>();
}
