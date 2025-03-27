package de.hsesslingen.focusflowbackend.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class TestMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String text;
}
