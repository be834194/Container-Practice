package com.practice.container.controller;

import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.practice.container.entity.Todo;
import com.practice.container.service.TodoService;

@AutoConfigureMockMvc
@AutoConfigureMybatis
@Testcontainers //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
@Transactional
@WebMvcTest(controllers = IndexController.class,
            includeFilters= @Filter(type = FilterType.ASSIGNABLE_TYPE,
			                        value = {TodoService.class}))
public class IndexControllerJoinTest {

    @Autowired
	private MockMvc mockMvc;

    @Container
    private static final MySQLContainer<?> mysql = 
        new MySQLContainer<>(DockerImageName.parse("mysql:8.0")) //??????????????????????????????Docker????????????????????????
                                            .withUsername("todo_user")
                                            .withPassword("docker_ci")
                                            .withDatabaseName("todo_list"); 
    
    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        //???????????????????????????????????????????????????????????????
        //add(String name,Supplier<Object>)
        registry.add("spring.datasource.url", mysql::getJdbcUrl); //??????????????????
    }

    @Test
    void isRunning() throws Exception{
        assertTrue(mysql.isRunning());
    }

    @Test
    void getAllTodoList() throws Exception{
        mockMvc.perform(get("/"))
               .andExpect(status().is2xxSuccessful())
               .andExpect(model().attribute("todoArr",hasSize(2)))
               .andExpect(model().attribute("todoArr",
                                            hasItem(hasProperty("todoName",is("??????????????????????????????")))
                                            )
                          )
               .andExpect(model().attributeExists("todoForm"))
               .andExpect(view().name("Index")).andReturn();
    }

    @Test
    void  isInserted() throws Exception{
        Todo todo = new Todo();
        todo.setTodoName("?????????15??????2?????????");
        mockMvc.perform(post("/add")
                       .flashAttr("todoForm", todo)
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "??????????????????????????????"))
                .andExpect(redirectedUrl("/")).andReturn();
    }

    @Test
    void  isDeleted() throws Exception{
        mockMvc.perform(post("/complete")
                       .param("todoId","2")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().is3xxRedirection())
               .andExpect(model().hasNoErrors())
               .andExpect(flash().attribute("message","?????????2????????????????????????"))
               .andExpect(redirectedUrl("/"));
    }
    
}