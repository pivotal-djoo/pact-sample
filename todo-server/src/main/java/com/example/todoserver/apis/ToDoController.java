package com.example.todoserver.apis;

import com.example.todoserver.ToDoService;
import com.example.todoserver.models.ToDo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@Controller
public class ToDoController {
    private final ToDoService toDoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping(value = "/todos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ToDo> getAllToDos() {
        log.debug("GET /todos called");
        return toDoService.getAllToDos();
    }

    @PostMapping(value = "/todos", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ToDo createToDo(@RequestBody ToDo toDo) throws JsonProcessingException {
        log.debug("POST /todos called with " + objectMapper.writeValueAsString(toDo));
        return toDoService.createToDo(toDo);
    }

    @PutMapping(value = "/todos", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ToDo updateToDo(@RequestBody ToDo toDo) throws JsonProcessingException {
        log.debug("PUT /todos called with " + objectMapper.writeValueAsString(toDo));
        return toDoService.updateToDo(toDo);
    }

    @DeleteMapping(value = "/todos/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteToDo(@PathVariable("id") Long id) throws JsonProcessingException {
        log.debug("DELETE /todos called with id: " + objectMapper.writeValueAsString(id));
        toDoService.deleteToDo(id);
    }
}
