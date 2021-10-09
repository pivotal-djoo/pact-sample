package com.example.todoserver;

import com.example.todoserver.models.ToDo;
import com.example.todoserver.persistence.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {
    @Mock
    private ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoService subject;

    @Test
    public void getAllToDo_returnsAllToDo() {
        List<ToDo> toDos = asList(
                ToDo.builder().text("Pick up milk").build(),
                ToDo.builder().text("Pick up catfood").build()
        );
        when(toDoRepository.findAll()).thenReturn(toDos);

        List<ToDo> actualToDo = subject.getAllToDos();
        assertThat(actualToDo.size()).isEqualTo(2);
        assertThat(actualToDo.get(0).getText()).isEqualTo(toDos.get(0).getText());
        assertThat(actualToDo.get(1).getText()).isEqualTo(toDos.get(1).getText());
    }

    @Test
    public void newToDo_savesNewToDoToRepository() {
        ToDo newToDo = ToDo.builder().text("Pick up milk").build();
        ToDo expectedSavedToDo = ToDo.builder().id(1L).text(newToDo.getText()).build();
        when(toDoRepository.save(newToDo)).thenReturn(expectedSavedToDo);

        ToDo savedToDo = subject.createToDo(newToDo);
        assertThat(savedToDo).isEqualTo(expectedSavedToDo);
        verify(toDoRepository).save(newToDo);
    }

    @Test
    public void updateToDo_updatesExistingToDoToInRepository() {
        ToDo updatedToDo = ToDo.builder().id(1L).text("Pick up milk").checked(true).build();
        when(toDoRepository.save(updatedToDo)).thenReturn(updatedToDo);

        ToDo savedToDo = subject.updateToDo(updatedToDo);
        assertThat(savedToDo).isEqualTo(updatedToDo);
        verify(toDoRepository).save(updatedToDo);
    }

    @Test
    public void deleteToDo_deletesExistingToDoToInRepository() {
        ToDo deletedToDo = ToDo.builder().id(1L).text("Pick up milk").checked(true).build();

        subject.deleteToDo(deletedToDo.getId());

        verify(toDoRepository).deleteById(deletedToDo.getId());
    }
}
