package com.todo.services;

import com.todo.models.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServices {
    Logger logger = LoggerFactory.getLogger(TodoServices.class);
    private List<Todo> todos=new ArrayList<>();
    public Todo createTodo(Todo todo){
        logger.info("[todo service], {}",todo);
        todos.add(todo);
        return todo;
    }

    public List<Todo> getAllTodos(){
        return todos;
    }

    public Todo getTodoById(int id){
        Todo todo1 = todos.stream().filter(todo -> todo.getId() == id).findFirst().get();
        return todo1;
    }

    public void deleteTodoById(int id){
        todos= todos.stream().filter(todo -> todo.getId() != id).collect(Collectors.toList());
    }

    public Todo updateTodoById(int id,Todo updatedTodo){
        todos=todos.stream().map(todo -> {
            if(todo.getId()==id){
                todo.setTitle(updatedTodo.getTitle());
                todo.setContent(updatedTodo.getContent());
                todo.setStatus(updatedTodo.getStatus());
                updatedTodo.setId(id);
                return todo;
            }else {
                return todo;
            }
        }).collect(Collectors.toList());
        return updatedTodo;
    }
}
