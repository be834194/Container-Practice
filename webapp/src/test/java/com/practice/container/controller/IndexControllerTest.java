package com.practice.container.controller;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.practice.container.entity.Todo;
import com.practice.container.service.TodoService;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class IndexControllerTest {

    @Autowired
	private MockMvc mockMvc;

    //Mock
    TodoService todoService = Mockito.mock(TodoService.class);

    @InjectMocks
    IndexController indexController;

    @BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
	}

    @Test
	void getAllTodoList() throws Exception{
        List<Todo> todoArr = new ArrayList<Todo>();
        Todo todo = new Todo(1,"test1");
        todoArr.add(todo);
        todo = new Todo(2,"test2");
        todoArr.add(todo);
        when(todoService.findTodoList()).thenReturn(todoArr);

    	mockMvc.perform(get("/"))
    	       .andExpect(status().is2xxSuccessful())
               .andExpect(model().attribute("todoArr",hasSize(2)))
               .andExpect(model().attribute("todoArr",
                                            hasItem(hasProperty("todoName",is("test2")))
                                            )
                         )
    	       .andExpect(model().attributeExists("todoForm"))
               .andExpect(view().name("Index"));
        verify(todoService,times(1)).findTodoList();
    }

    @Test
    void  isInserted() throws Exception{
        Todo todo = new Todo(3,"task3");
        when(todoService.insertTodo(any(Todo.class))).thenReturn("タスクを追加しました");
        
        mockMvc.perform(post("/add")
					   .flashAttr("todoForm", todo)
					   .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(status().is3xxRedirection())
               .andExpect(model().hasNoErrors())
               .andExpect(flash().attribute("message", "タスクを追加しました"))
               .andExpect(redirectedUrl("/"));
        verify(todoService,times(1)).insertTodo(any(Todo.class));
    }

    @Test
    public void isDeleted() throws Exception{
        when(todoService.deleteTodo(2)).thenReturn("タスク2番を削除しました");

        mockMvc.perform(post("/complete")
                       .param("todoId","2")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
               .andExpect(status().is3xxRedirection())
               .andExpect(model().hasNoErrors())
               .andExpect(flash().attribute("message", "タスク2番を削除しました"))
               .andExpect(redirectedUrl("/"));
        verify(todoService,times(1)).deleteTodo(2);
    }
    
}
