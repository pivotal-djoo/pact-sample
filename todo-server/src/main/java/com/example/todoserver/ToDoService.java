package com.example.todoserver;

import com.example.todoserver.models.ToDo;
import com.example.todoserver.persistence.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    private ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo createToDo(ToDo newToDo) {
        ToDo toDo = toDoRepository.save(newToDo);
        return toDo;
    }

    public ToDo updateToDo(ToDo updatedToDo) {
        ToDo toDo = toDoRepository.save(updatedToDo);
        return toDo;
    }

    public void deleteToDo(Long toDoId) {
        toDoRepository.deleteById(toDoId);
    }
}
