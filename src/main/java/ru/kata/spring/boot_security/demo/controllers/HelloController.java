package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    @GetMapping({"/", "/index"})
    public String sayWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Welcome!");
        messages.add("Practical challenge 3.1.3 Java pre-project.");
        messages.add("Task 3.1.3. Spring Boot + Security.");
        model.addAttribute("messages", messages);

        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }
}
