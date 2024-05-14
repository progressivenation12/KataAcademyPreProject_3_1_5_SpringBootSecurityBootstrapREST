package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public String showUserInfo(@AuthenticationPrincipal UserDetails userDetails, Model model, Principal principal) {

        model.addAttribute("isAdmin", userDetails.getAuthorities().stream()
                .anyMatch(admin -> admin.getAuthority().equals("ROLE_ADMIN")));

        Person person = (Person) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("person", person);

        return "user";
    }
}
