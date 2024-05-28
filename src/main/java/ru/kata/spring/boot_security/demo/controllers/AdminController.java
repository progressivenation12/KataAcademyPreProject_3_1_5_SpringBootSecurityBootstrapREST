package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final RoleService roleService;
    private final UserDetailsService userDetailsService;

    public AdminController(PeopleService peopleService, PersonValidator personValidator, RoleService roleService, UserDetailsService userDetailsService) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/create")
    public String createPage(@ModelAttribute("person") Person person, Model model, Principal principal) {
        Person personAdmin = (Person) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("personAdmin", personAdmin);
        model.addAttribute("roles", roleService.getAllRoles());
        return "registration";
    }

    @PostMapping("/save")
    public String postCreate(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult, Model model, Principal principal) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            Person personAdmin = (Person) userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("personAdmin", personAdmin);
            model.addAttribute("roles", roleService.getAllRoles());
            return "registration";
        }

        peopleService.createNewUser(person);

        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String postEdit(@ModelAttribute("person") @Valid Person person,
                           BindingResult bindingResult, Model model, Principal principal) {

        person.setOldUserName(person.getUserName());

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            Person currentPerson = (Person) userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("currentPerson", currentPerson);

            model.addAttribute("person", person);
            model.addAttribute("people", peopleService.getUsersList());
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("org.springframework.validation.BindingResult.person", bindingResult);
            return "admin";
        }

        peopleService.updateUser(person.getId(), person);

        return "redirect:/admin";
    }

    @GetMapping
    public String getAllUsers(Model model, Principal principal) {
        Person currentPerson = (Person) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("currentPerson", currentPerson);
        model.addAttribute("people", peopleService.getUsersList());
        model.addAttribute("roles", roleService.getAllRoles());

        return "admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {
        peopleService.deleteUser(id);
        return "redirect:/admin";
    }
}
