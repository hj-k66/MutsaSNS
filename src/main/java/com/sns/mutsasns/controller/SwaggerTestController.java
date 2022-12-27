package com.sns.mutsasns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwaggerTestController {

    @GetMapping("/api/v1/hello")
    public String hello(){
        return "김희정";
    }
    @GetMapping("/api/v1/bye")
    public String bye(){
        return "bye";
    }
}
