package com.springboot.controllers;

import com.springboot.model.Student;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        ResponseEntity<Student> responseEntity=new ResponseEntity<Student>(student, HttpStatus.CREATED);
        return responseEntity;
    }
}
