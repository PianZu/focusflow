package de.hsesslingen.focusflowbackend.controller;

import de.hsesslingen.focusflowbackend.model.TestMessage;
import de.hsesslingen.focusflowbackend.repository.TestMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class TestMessageController {

    @Autowired
    private TestMessageRepository messageRepository;

    @GetMapping
    public List<TestMessage> getMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    public TestMessage addMessage(@RequestBody TestMessage message) {
        return messageRepository.save(message);
    }
}
