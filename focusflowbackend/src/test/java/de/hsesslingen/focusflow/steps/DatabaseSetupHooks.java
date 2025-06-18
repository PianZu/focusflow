package de.hsesslingen.focusflow.steps;

import de.hsesslingen.focusflowbackend.FocusflowbackendApplication;
import de.hsesslingen.focusflowbackend.repository.TaskRepository;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = FocusflowbackendApplication.class)
@ActiveProfiles("test")
@Transactional
// This class is responsible for setting up the database before each test scenario
public class DatabaseSetupHooks {

    private static final Logger LOGGER_HOOK = LoggerFactory.getLogger(DatabaseSetupHooks.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Before(order = 0) 
    public void cleanDatabaseForAllScenarios() {
        LOGGER_HOOK.info("<<<<< GLOBAL CLEANUP @Before(order = 0): Cleaning database tables... >>>>>");
        taskRepository.deleteAllInBatch();    
        teamRepository.deleteAllInBatch();    
        userRepository.deleteAllInBatch();    
        userRepository.flush(); 
        LOGGER_HOOK.info("<<<<< GLOBAL CLEANUP @Before(order = 0): Database cleaning complete. >>>>>");
    }
}