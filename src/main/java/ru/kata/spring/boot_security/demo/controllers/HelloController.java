package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.security.PersonDetails;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
    @GetMapping({"/", "/index"})
    public String sayWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Welcome!");
        messages.add("Practical challenge 3.1.4 Java pre-project.");
        messages.add("Task 3.1.4. Spring Boot + Security + Bootstrap.");
        model.addAttribute("messages", messages);

        return "index";
    }

    @GetMapping("/user")
    public String showUserInfo(@AuthenticationPrincipal PersonDetails personDetails, Model model) {
        if (personDetails != null) {
            model.addAttribute("id", personDetails.getPerson().getId());
            model.addAttribute("username", personDetails.getPerson().getUserName());
            model.addAttribute("age", personDetails.getPerson().getAge());
            model.addAttribute("email", personDetails.getPerson().getEmail());
            model.addAttribute("roleSet", personDetails.getPerson().getRoleSet());
        }

        return "users/user";
    }
}
