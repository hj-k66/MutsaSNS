package com.sns.mutsasns.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class SwaggerTestController {

    @GetMapping("/{num}")
    public ResponseEntity<Integer> sumOfDigit(@PathVariable int num){
        int sum = 0;
        while(num > 0){
            sum += num%10;
            num /= 10;
        }
        return ResponseEntity.ok().body(sum);
    }

    @GetMapping("")
    public String hello(){
        return "김희정";
    }
}
