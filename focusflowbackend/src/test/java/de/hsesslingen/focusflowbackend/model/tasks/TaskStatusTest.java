package de.hsesslingen.focusflowbackend.model.tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskStatusTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void testTaskStatusTransition() {
        task.setStatus(TaskStatus.OPEN);
        task.setStatus(TaskStatus.IN_REVIEW);
        assertEquals(TaskStatus.IN_REVIEW, task.getStatus());
    }
}
