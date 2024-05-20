package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
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

    @GetMapping("/admin/create")
    public String createPage(@ModelAttribute("person") Person person, Model model, Principal principal) {
        Person personAdmin = (Person) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("personAdmin", personAdmin);
        model.addAttribute("roles", roleService.getAllRoles());
        return "registration";
    }

    @PostMapping("/admin/save")
    public String postCreate(@ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult, Model model, Principal principal) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            Person personAdmin = (Person) userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("personAdmin", personAdmin);
            model.addAttribute("roles", roleService.getAllRoles());
            System.out.println("Error create");
            return "registration";
        }

        peopleService.createNewUser(person);

        return "redirect:/admin";
    }

//    @GetMapping("/admin/edit")
//    public String editPage(@RequestParam("id") int id, Model model) {
//        Person editPerson = peopleService.getUserByID(id);
//
//        editPerson.setOldUserName(editPerson.getUserName());
//
//        model.addAttribute("editPerson", editPerson);
//        model.addAttribute("roles", roleService.getAllRoles());
//        return "edit";
//    }

    @PostMapping("/admin/update")
    public String postEdit(@ModelAttribute("updateUser") @Valid Person updateUser,
                           BindingResult bindingResult, Model model, Principal principal) {

        updateUser.setOldUserName(updateUser.getUserName());

        personValidator.validate(updateUser, bindingResult);

        if (bindingResult.hasErrors()) {
            Person currentPerson = (Person) userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("currentPerson", currentPerson);

            model.addAttribute("updateUser", updateUser);
            model.addAttribute("people", peopleService.getUsersList());
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "admin";
        }

        peopleService.updateUser(updateUser.getId(), updateUser);

        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model, Principal principal) {
        Person currentPerson = (Person) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("currentPerson", currentPerson);
        model.addAttribute("people", peopleService.getUsersList());
        model.addAttribute("allRoles", roleService.getAllRoles());

        Map<Integer, String> personRolesMap = new HashMap<>();
        for (Person person : peopleService.getUsersList()) {
            String rolesString = person.getRoleSet().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.joining(","));
            personRolesMap.put(person.getId(), rolesString);
        }
        model.addAttribute("personRolesMap", personRolesMap);

        return "admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") int id) {
        peopleService.deleteUser(id);
        return "redirect:/admin";
    }

}
