package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;

@Controller
public class AdminController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final RoleService roleService;
    private final PeopleRepository peopleRepository;

    public AdminController(PeopleService peopleService, PersonValidator personValidator, RoleService roleService, PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.roleService = roleService;
        this.peopleRepository = peopleRepository;
    }

    @GetMapping("/admin/create")
    public String createPage(@ModelAttribute("person") Person person, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        System.out.println("/admin/create " + person + roleService.getAllRoles());
        return "registration";
    }

    @PostMapping("/admin/save")
    public String postCreate(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult, Model model) {
        System.out.println("/admin/save " + person + roleService.getAllRoles());

        if (peopleRepository.findAllWithRoles().size() != 0) {
            personValidator.validate(person, bindingResult);
            System.out.println("personValidator");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            System.out.println("Error create");
            return "registration";
        }

        peopleService.createNewUser(person);

        System.out.println("postCreate" + person);

        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("person", peopleService.getUserByID(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PostMapping("/admin/update")
    public String postEdit(@ModelAttribute("person") @Valid Person person,
                           BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit";
        }

        peopleService.updateUser(person.getId(), person);
        System.out.println(person);
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("people", peopleService.getUsersList());
        return "admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") int id) {
        peopleService.deleteUser(id);
        return "redirect:/admin";
    }
}
