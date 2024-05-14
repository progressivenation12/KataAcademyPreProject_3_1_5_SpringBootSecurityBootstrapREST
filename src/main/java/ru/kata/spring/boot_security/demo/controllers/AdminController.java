package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;

@Controller
public class AdminController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public AdminController(PeopleService peopleService, PersonValidator personValidator, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

//    @GetMapping("/admin/reg_edit")
//    public String registrationOrEditPage(@RequestParam(name = "id", required = false) Integer id, Model model) {
//
//        if (id == null) {
//            model.addAttribute("person", new Person());
//            model.addAttribute("roles", roleService.getAllRoles());
//        } else {
//            model.addAttribute("person", peopleService.getUserByID(id));
//            model.addAttribute("roles", roleService.getAllRoles());
//        }
//        return "regEdit";
//    }

    @GetMapping("/admin/edit")
    public String editPage(@RequestParam("id") int id, Model model) {
            model.addAttribute("person", peopleService.getUserByID(id));
            model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    // реализация пока только редактирование пользователя
    @PostMapping("/admin/update")
    public String postEdit(@ModelAttribute("person") @Valid Person person,
                                         BindingResult bindingResult, @RequestParam("id") int id) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        peopleService.updateUser(id, person);

        return "redirect:/admin";
    }

//    @PostMapping("/admin/reg_edit")
//    public String postRegistrationOrEdit(@ModelAttribute("person") @Valid Person person,
//                                         @RequestParam(value = "roles", required = false) Set<Role> roles,
//                                         BindingResult bindingResult,
//                                         Model model) {
//        System.out.println("personValidator");
//        personValidator.validate(person, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("roles", roleService.getAllRoles());
//            return "regEdit";
//        }
//        System.out.println("bindingResult.hasErrors()");
//        if (roles == null || roles.isEmpty()) {
//            model.addAttribute("roleNotSelectedMessage", "Роль не выбрана! Выберите хотя бы одну роль");
//            model.addAttribute("roles", roleService.getAllRoles());
//            return "regEdit";
//        }
//        System.out.println("roles == null || roles.isEmpty()");
//
//        person.setPassword(passwordEncoder.encode(person.getPassword()));
//        System.out.println(person);
//
//        peopleService.createNewUser(person);
//
//        return "redirect:/admin";
//    }

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
