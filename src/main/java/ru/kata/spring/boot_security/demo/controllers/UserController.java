package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.Person;

import java.security.Principal;

@Controller
public class UserController {
    private final UserDetailsService userDetailsService;

    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
//        Person person = (Person) userDetailsService.loadUserByUsername(principal.getName());
//        model.addAttribute("person", person);

        return "user";
    }

    @GetMapping("/admin/user")
    public String showAdminInfo(Model model, Principal principal) {
//        Person person = (Person) userDetailsService.loadUserByUsername(principal.getName());
//        model.addAttribute("person", person);

        return "user-admin";
    }
}
