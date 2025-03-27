package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.TestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestMessageRepository extends JpaRepository<TestMessage, Long> {}