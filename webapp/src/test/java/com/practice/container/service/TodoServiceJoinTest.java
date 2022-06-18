package com.practice.container.service;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.practice.container.entity.Todo;

@SpringBootTest
@Transactional
@Testcontainers //テストコンテナを有効にする。宣言したコンテナは、テストメソッド間で共有される
public class TodoServiceJoinTest {

    @Autowired
    TodoService todoService;

    @Container
    private static final MySQLContainer<?> mysql = 
        new MySQLContainer<>(DockerImageName.parse("mysql:8.0")) //指定された文字列からDockerイメージ名を解析
                                            .withUsername("todo_user")
                                            .withPassword("docker_ci")
                                            .withDatabaseName("todo_list"); 
    
    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        //起動中のコンテナへ接続するための情報を設定
        //add(String name,Supplier<Object>)
        registry.add("spring.datasource.url", mysql::getJdbcUrl); //メソッド参照
    }

    @Test
    void isRunning() throws Exception{
        assertTrue(mysql.isRunning());
    }

    @Test
    void getAllTodoList() throws Exception{
        List<Todo> todoArr = todoService.findTodoList();
        assertEquals(2,todoArr.size());
        assertEquals(1,todoArr.get(0).getTodoId());
        assertEquals("服をクリーニングに出す",todoArr.get(0).getTodoName());
        assertEquals(2,todoArr.get(1).getTodoId());
        assertEquals("きのこのマリネを作る",todoArr.get(1).getTodoName());
    }

    @Test
    void isInserted() throws Exception{
        Todo todo = new Todo();
        todo.setTodoName("腕立て15回を2セット");
        String message = todoService.insertTodo(todo);
        assertEquals("タスクを追加しました", message);

        List<Todo> todoArr = todoService.findTodoList();
        assertEquals(3,todoArr.get(2).getTodoId());
        assertEquals("腕立て15回を2セット",todoArr.get(2).getTodoName());
    }

    @Test
    void isDeleted() throws Exception{
        String message = todoService.deleteTodo(2);
        assertEquals("タスク2番を削除しました", message);

        List<Todo> todoArr = todoService.findTodoList();
        assertEquals(1,todoArr.size());
    }
    
}
