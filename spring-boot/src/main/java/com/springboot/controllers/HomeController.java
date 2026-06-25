package com.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {
    @Value("${server.serverName}")
    private String serverName;

    @Autowired
    private ApplicationConfig applicationConfig;

    @RequestMapping("/todos")
    public List<String> justTest(){
        List<String>todos=List.of("Janakpur","Delhi","Gurugram","rishikesh",serverName);
        return todos;
    }

    @RequestMapping("/config")
    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }
}

