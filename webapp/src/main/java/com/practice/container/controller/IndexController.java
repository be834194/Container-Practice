package com.practice.container.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practice.container.entity.Todo;
import com.practice.container.service.TodoService;

@Controller
public class IndexController {

	private final TodoService todoService;

	public IndexController(TodoService todoService){
		this.todoService = todoService;
	}

    @GetMapping("/")
	public String showIndex(@ModelAttribute("todoForm") Todo todo,Model model) {
		List<Todo> todoArr = todoService.findTodoList();
		model.addAttribute("todoArr", todoArr);
		return "Index";
	}

	@PostMapping("/add")
	public String insertTask(@ModelAttribute("todoForm") Todo todo,
	                         RedirectAttributes model){
		String message = todoService.insertTodo(todo);
		model.addFlashAttribute("message", message);
		return "redirect:/";
	}

	@PostMapping("/complete")
	public String deleteTask(int todoId,
	                         RedirectAttributes model){
		String message = todoService.deleteTodo(todoId);
		model.addFlashAttribute("message", message);
		return "redirect:/";
	}
    
}
