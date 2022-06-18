package com.practice.container.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.container.entity.Todo;
import com.practice.container.repository.TodoRepository;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public List<Todo> findTodoList(){
        List<Todo> todoArr = todoRepository.findTodoList();
        return todoArr;
    }

    public String insertTodo(Todo todo){
        todoRepository.insertTodo(todo);
        return "タスクを追加しました";
    }

    public String deleteTodo(int id){
        todoRepository.deleteTodo(id);
        return "タスク" + id + "番を削除しました";
    }

}
