package com.practice.container.repository;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.practice.container.entity.Todo;

@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers //テストコンテナを有効にする。宣言したコンテナは、テストメソッド間で共有される
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Container
    private static final MySQLContainer<?> mysql = 
        new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                                            .withUsername("todo_user")
                                            .withPassword("docker_ci")
                                            .withDatabaseName("todo_list"); 
    
    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        // コンテナで起動中のMySQLへ接続するためのJDBC URLをプロパティへ設定
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
    }

    @Test
    void isRunning() throws Exception{
        assertTrue(mysql.isRunning());
    }

    @Test
    void getAllTodoList() throws Exception{
        List<Todo> todoArr = todoRepository.findTodoList();
        assertEquals(todoArr.size(),2);
        assertEquals(todoArr.get(0).getTodoId(),1);
        assertEquals(todoArr.get(0).getTodoName(),"服をクリーニングに出す");
        assertEquals(todoArr.get(1).getTodoId(),2);
        assertEquals(todoArr.get(1).getTodoName(),"きのこのマリネを作る");
    }

    @Test
    void isInserted() throws Exception{
        Todo todo = new Todo();
        todo.setTodoName("腕立て15回を2セット");
        todoRepository.insertTodo(todo);

        List<Todo> todoArr = todoRepository.findTodoList();
        assertEquals(todoArr.get(2).getTodoId(),3);
        assertEquals(todoArr.get(2).getTodoName(),"腕立て15回を2セット");
    }

}
