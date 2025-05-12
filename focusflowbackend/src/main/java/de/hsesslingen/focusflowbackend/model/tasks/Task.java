package de.hsesslingen.focusflowbackend.model.tasks;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;

@Entity
@Data
@Table(name = "tasks")
// This class represents a task in the system
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    @JsonManagedReference
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonBackReference
    // The task can be assigned to a team, but it's not mandatory
    private Team team;   
}
