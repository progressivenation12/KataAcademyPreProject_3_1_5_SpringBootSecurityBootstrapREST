package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.service.PeopleService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
    private final PeopleService peopleService;

    public HelloController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping({"/", "/index"})
    public String sayWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Welcome!");
        messages.add("Practical challenge 3.1.3 Java pre-project.");
        messages.add("Task 3.1.3. Spring Boot + Security.");
        model.addAttribute("messages", messages);

        return "index";
    }

    @GetMapping("/user")
    public String showUserInfo(@AuthenticationPrincipal UserDetails userDetails, Model model, Principal principal) {

        model.addAttribute("isAdmin", userDetails.getAuthorities().stream()
                .anyMatch(admin -> admin.getAuthority().equals("ROLE_ADMIN")));

        Person person = peopleService.getUserByUsername(principal.getName());
        model.addAttribute("person", person);

        return "users/user";
    }
}
