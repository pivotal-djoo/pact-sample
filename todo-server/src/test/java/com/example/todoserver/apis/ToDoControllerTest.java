package com.example.todoserver.apis;

import com.example.todoserver.ToDoService;
import com.example.todoserver.models.ToDo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = ToDoController.class)
class ToDoControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    ToDoService toDoService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getToDos_returnsToDos() throws Exception {
        List<ToDo> todos = asList(ToDo.builder().text("Pick up bread").checked(true).build(),
                ToDo.builder().text("Grab some butter").build());
        when(toDoService.getAllToDos()).thenReturn(todos);

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

        when(toDoService.createToDo(toDo)).thenReturn(toDo);

        mvc.perform(post("/todos")
                .content(objectMapper.writeValueAsString(toDo))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":null,\"text\":\"Pick up catfood\",\"checked\":true}"));

        verify(toDoService).createToDo(toDo);
    }

    @Test
    public void updateToDo_updatesExistingToDo() throws Exception {
        ToDo toDo = ToDo.builder().text("Pick up catfood").checked(true).build();

        when(toDoService.updateToDo(toDo)).thenReturn(toDo);

        mvc.perform(put("/todos")
                .content(objectMapper.writeValueAsString(toDo))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{\"id\":null,\"text\":\"Pick up catfood\",\"checked\":true}"));

        verify(toDoService).updateToDo(toDo);
    }

    @Test
    public void deleteToDo_deletesExistingToDo() throws Exception {
        ToDo toDo = ToDo.builder().id(1L).text("Pick up catfood").checked(true).build();

        mvc.perform(delete("/todos/" + toDo.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());

        verify(toDoService).deleteToDo(toDo.getId());
    }
}
