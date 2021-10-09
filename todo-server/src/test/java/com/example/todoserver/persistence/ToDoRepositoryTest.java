package com.example.todoserver.persistence;

import com.example.todoserver.TodoServerApplication;
import com.example.todoserver.models.ToDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = TodoServerApplication.class)
class ToDoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToDoRepository subject;

    @Test
    public void findAll_returnsAllToDos() {
        ToDo todo1 = ToDo.builder().text("Pick up oranges").build();
        ToDo todo2 = ToDo.builder().text("Bring up catfood").build();
        entityManager.persistAndFlush(todo1);
        entityManager.persistAndFlush(todo2);

        List<ToDo> actualToDo = subject.findAll();
        assertThat(actualToDo.size()).isEqualTo(2);
        assertThat(actualToDo.get(0).getText()).isEqualTo(todo1.getText());
        assertThat(actualToDo.get(1).getText()).isEqualTo(todo2.getText());
    }

    @Test
    public void save_savesThenReturnsToDo() {
        ToDo newToDo = ToDo.builder().text("Pick up fruits").build();
        ToDo savedToDo = subject.save(newToDo);

        assertThat(savedToDo.getText()).isEqualTo(newToDo.getText());
        assertThat(entityManager.find(ToDo.class, savedToDo.getId())).isEqualTo(savedToDo);
    }

    @Test
    public void delete_removesToDo() {
        ToDo todo = ToDo.builder().text("Pick up fruits").build();
        entityManager.persistAndFlush(todo);
        assertThat(entityManager.find(ToDo.class, todo.getId())).isNotNull();

        subject.delete(todo);

        assertThat(entityManager.find(ToDo.class, todo.getId())).isNull();
    }
}
