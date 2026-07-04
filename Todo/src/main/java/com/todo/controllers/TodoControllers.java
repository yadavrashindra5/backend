package com.todo.controllers;

import com.todo.models.Todo;
import com.todo.services.TodoServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/todos")
public class TodoControllers {
    Logger logger = LoggerFactory.getLogger(TodoControllers.class);

    @Autowired
    private TodoServices todoServices;

    Random random=new Random();

//    create todos
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo){
        logger.info("[ todos controller ] {}",todo);

        int id=random.nextInt(9999);
        logger.info("id {}",id);
        todo.setId(id);

        Todo todo1=todoServices.createTodo(todo);
        return new ResponseEntity<>(todo1, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(){
        List<Todo> allTodos = todoServices.getAllTodos();
        ResponseEntity<List<Todo>>responseEntity=new ResponseEntity<>(allTodos, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable int id){
        Todo todoById = todoServices.getTodoById(id);
        return new ResponseEntity<>(todoById, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteTodoById(@PathVariable int id){
        todoServices.deleteTodoById(id);
        Map<String,String> map=Map.of("message","deleted");
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable int id,@RequestBody Todo todo){
        Todo todo1=todoServices.updateTodoById(id,todo);
        return new ResponseEntity<>(todo1,HttpStatus.OK);
    }
}
