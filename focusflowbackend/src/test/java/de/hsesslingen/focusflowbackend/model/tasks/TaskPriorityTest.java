package de.hsesslingen.focusflowbackend.model.tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskPriorityTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void testTaskPriorityTransition() {
        task.setPriority(TaskPriority.LOW);
        task.setPriority(TaskPriority.MEDIUM);
        assertEquals(TaskPriority.MEDIUM, task.getPriority());
    }
}
