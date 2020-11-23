package com.rufino.server;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/")
public class HomeController {
    
    @GetMapping
    public String home() {
        JSONObject json = new JSONObject();
        json.put("message","Hello World from Spring Boot" );
        return json.toString();
    }
}
