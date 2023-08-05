package com.project.stocker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {

    @GetMapping("api/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
}
