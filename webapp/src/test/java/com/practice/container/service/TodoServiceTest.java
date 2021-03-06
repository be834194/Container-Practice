package com.practice.container.service;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.practice.container.entity.Todo;
import com.practice.container.repository.TodoRepository;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    //Mock
    TodoRepository todoRepository = Mockito.mock(TodoRepository.class);

    @InjectMocks
    TodoService todoService;

    @Test
    public void getAllTodoList() throws Exception{
        List<Todo> todoArr = new ArrayList<Todo>();
        Todo todo = new Todo(1,"test1");
        todoArr.add(todo);
        todo = new Todo(2,"test2");
        todoArr.add(todo);
        when(todoRepository.findTodoList()).thenReturn(todoArr);

        List<Todo> resultArr = todoService.findTodoList();
        assertEquals(2, resultArr.size());
        verify(todoRepository,times(1)).findTodoList();
    }

    @Test
    public void isInserted() throws Exception{
        Todo todo = new Todo(3,"new task");
        String message = todoService.insertTodo(todo);
        doNothing().when(todoRepository).insertTodo(any(Todo.class));

        assertEquals("タスクを追加しました", message);
        verify(todoRepository,times(1)).insertTodo(any(Todo.class));
    }

    @Test
    public void isDeleted() throws Exception{
        String message = todoService.deleteTodo(2);
        doNothing().when(todoRepository).deleteTodo(2);

        assertEquals("タスク2番を削除しました", message);
        verify(todoRepository,times(1)).deleteTodo(2);
    }
    
}
