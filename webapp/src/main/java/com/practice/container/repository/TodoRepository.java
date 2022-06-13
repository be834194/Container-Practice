package com.practice.container.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.practice.container.entity.Todo;

@Mapper
public interface TodoRepository {

    List<Todo> findTodoList();
    void insertTodo(Todo todo);
    void deleteTodo(int id);

}
