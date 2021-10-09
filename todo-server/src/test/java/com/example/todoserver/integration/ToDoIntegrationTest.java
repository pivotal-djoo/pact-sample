package com.example.todoserver.integration;

import com.example.todoserver.models.ToDo;
import com.example.todoserver.persistence.ToDoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ToDoIntegrationTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private MockMvc mvc;

    @AfterEach
    void tearDown() {
        toDoRepository.deleteAll();
    }

    @Test
    public void getToDos_returnsToDos() throws Exception {
        List<ToDo> todos = asList(
                ToDo.builder().text("Pick up bread").checked(true).build(),
                ToDo.builder().text("Grab some butter").build()
        );
        toDoRepository.saveAll(todos);

        MockHttpServletResponse response = mvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<ToDo> actualToDo = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<ToDo>>() {
        });

        assertThat(actualToDo).hasSize(2);
        assertThat(actualToDo.get(0).getText()).isEqualTo("Pick up bread");
        assertThat(actualToDo.get(0).isChecked()).isTrue();
        assertThat(actualToDo.get(1).getText()).isEqualTo("Grab some butter");
    }

    @Test
    public void createToDo_createsNewToDo() throws Exception {
        ToDo toDo = ToDo.builder().text("Pick up catfood").checked(true).build();

        mvc.perform(post("/todos")
                .content(objectMapper.writeValueAsString(toDo))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        List<ToDo> savedToDos = toDoRepository.findAll();
        assertThat(savedToDos.get(0).getText()).isEqualTo(toDo.getText());
        assertThat(savedToDos.get(0).isChecked()).isEqualTo(toDo.isChecked());
    }

    @Test
    public void updateToDo_updatesExistingToDo() throws Exception {
        ToDo toDo = toDoRepository.save(ToDo.builder().text("Pick up catfood").checked(false).build());
        toDo.setChecked(true);

        mvc.perform(put("/todos")
                .content(objectMapper.writeValueAsString(toDo))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());

        List<ToDo> savedToDos = toDoRepository.findAll();
        assertThat(savedToDos.get(0).getText()).isEqualTo(toDo.getText());
        assertThat(savedToDos.get(0).isChecked()).isEqualTo(toDo.isChecked());
    }

    @Test
    public void deleteToDo_removesExistingToDo() throws Exception {
        ToDo toDo = toDoRepository.save(ToDo.builder().text("Pick up catfood").checked(true).build());

        mvc.perform(delete("/todos/" + toDo.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());

        assertThat(toDoRepository.findAll()).hasSize(0);
    }
}
