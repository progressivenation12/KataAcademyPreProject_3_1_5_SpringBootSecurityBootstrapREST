package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundException;
import ru.kata.spring.boot_security.demo.models.PeopleAndRolesResponse;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class RestAdminController {
    private final PeopleService peopleService;
    private final RoleService roleService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public RestAdminController(PeopleService peopleService, RoleService roleService, PersonValidator personValidator, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.roleService = roleService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<PeopleAndRolesResponse> getAllUsers() {
        List<PersonDTO> people = peopleService.getAllUsers().stream().map(this::converteToPersonDTO)
                .collect(Collectors.toList());
        List<Role> roles = roleService.getAllRoles();

        PeopleAndRolesResponse peopleAndRolesResponse = new PeopleAndRolesResponse(people, roles);

        return new ResponseEntity<>(peopleAndRolesResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        PersonDTO personDTO = converteToPersonDTO(peopleService.getUserByID(id));

        if (personDTO == null) {
            throw new PersonNotFoundException("Такого пользователя с ID: " + id + " не существует в БД!");
        }

        return personDTO;
    }

    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody @Valid PersonDTO personDTO) {

        if (peopleService.getUserByUsername(personDTO.getUserName()) != null) {
            return new ResponseEntity<>("Имя пользователя уже занято!", HttpStatus.BAD_REQUEST);
        }

        Person person = converteToPerson(personDTO);

        Set<Role> roles = new HashSet<>();

        for (String roleName : personDTO.getRoleSet()) {
            Role role = roleService.getRoleByName(roleName);

            if (role == null) {
                return new ResponseEntity<>("Роль не найдена!", HttpStatus.BAD_REQUEST);
            }

            roles.add(role);
        }

        person.setRoleSet(roles);

        peopleService.addNewUser(person);

        return new ResponseEntity<>("Пользователь " + person.getUserName() + " успешно добавлен.", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody Person person, BindingResult bindingResult) {
        person.setOldUserName(peopleService.getUserByID(person.getId()).getUserName());
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError("userName")).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();

        for (Role role : person.getRoleSet()) {
            Role roleFromDb = roleService.getRoleByName(role.getRoleName());
            if (roleFromDb == null) {
                return new ResponseEntity<>("Роль не найдена: " + role.getRoleName(), HttpStatus.BAD_REQUEST);
            }
            roles.add(roleFromDb);
        }
        person.setRoleSet(roles);

        peopleService.updateUser(person.getId(), person);

        return new ResponseEntity<>("Данные пользователя " + person.getUserName() + " успешно обновлены.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {

        if (peopleService.getUserByID(id) == null) {
            throw new PersonNotFoundException("Такого пользователя с ID: " + id + " не существует в БД!");
        }

        String userName = peopleService.getUserByID(id).getUserName();

        peopleService.deleteUser(id);
        return new ResponseEntity<>("Пользователь " + userName + " успешно удалён.", HttpStatus.OK);
    }

    private Person converteToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
    private PersonDTO converteToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
