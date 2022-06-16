package com.practice.container.entity;

public class Todo {

    private int todoId;

    private String todoName;

    public void setTodoId(int id){
        this.todoId = id;
    }

    public int getTodoId(){
        return this.todoId;
    }

    public void setTodoName(String name){
        this.todoName = name;
    }

    public String getTodoName(){
        return this.todoName;
    }
    
}
