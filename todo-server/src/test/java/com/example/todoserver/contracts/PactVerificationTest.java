package com.example.todoserver.contracts;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.StateChangeAction;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.example.todoserver.models.ToDo;
import com.example.todoserver.persistence.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static java.util.Arrays.asList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("To Do API")
@PactBroker
public class PactVerificationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ToDoRepository toDoRepository;

    @BeforeEach
    void setup(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State(value = "test GET", action = StateChangeAction.SETUP)
    void getToDos() {
        toDoRepository.deleteAll();
        List<ToDo> todos = asList(
                ToDo.builder().text("Pick up pickles").checked(false).build(),
                ToDo.builder().text("Pick up kids").checked(true).build()
        );
        toDoRepository.saveAll(todos);
    }

    @State(value = "test POST", action = StateChangeAction.SETUP)
    void addToDo() {
        toDoRepository.deleteAll();
    }
}
