package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import ru.kata.spring.boot_security.demo.models.AdminResponse;
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
@RequestMapping("/api/admin")
public class RestAdminController {

    private final ModelMapper modelMapper;
    private final PeopleService peopleService;
    private final RoleService roleService;
    private final PersonValidator personValidator;

    @Autowired
    public RestAdminController(ModelMapper modelMapper, PeopleService peopleService, RoleService roleService, PersonValidator personValidator) {
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
        this.roleService = roleService;
        this.personValidator = personValidator;
    }

    @GetMapping("/all-users")
    public ResponseEntity<AdminResponse> getAllUsers() {
        List<PersonDTO> people = peopleService.getAllUsers().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
        List<Role> roles = roleService.getAllRoles();

        AdminResponse adminResponse = new AdminResponse(people, roles);

        return new ResponseEntity<>(adminResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        PersonDTO personDTO = convertToPersonDTO(peopleService.getUserByID(id));

        if (personDTO == null) {
            throw new PersonNotFoundException("Пользователя с ID: " + id + " не существует в БД!");
        }

        return personDTO;
    }

    @PostMapping
    public ResponseEntity<String> addNewUser(@RequestBody @Valid PersonDTO personDTO) {

        if (peopleService.getUserByUsername(personDTO.getUserName()) != null) {
            return new ResponseEntity<>("Имя пользователя уже занято!", HttpStatus.BAD_REQUEST);
        }

        Person person = convertToPerson(personDTO);

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

        return new ResponseEntity<>("Пользователь " + person.getUserName() + " успешно добавлен.", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody PersonDTO personDTO, BindingResult bindingResult) {
        personDTO.setOldUserName(peopleService.getUserByID(personDTO.getId()).getUserName());
        personValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError("userName")).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        Set<String> roles = new HashSet<>();

        for (String role : personDTO.getRoleSet()) {
            Role roleFromDb = roleService.getRoleByName(role);
            if (roleFromDb == null) {
                return new ResponseEntity<>("Роль не найдена: " + role, HttpStatus.BAD_REQUEST);
            }
            roles.add(String.valueOf(roleFromDb));
        }
        personDTO.setRoleSet(roles);

        peopleService.updateUser(personDTO.getId(), convertToPerson(personDTO));

        return new ResponseEntity<>("Данные пользователя " + personDTO.getUserName() + " успешно обновлены.", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {

        if (peopleService.getUserByID(id) == null) {
            throw new PersonNotFoundException("Пользователя с ID: " + id + " не существует в БД!");
        }

        String userName = peopleService.getUserByID(id).getUserName();

        peopleService.deleteUser(id);
        return new ResponseEntity<>("Пользователь " + userName + " успешно удалён.", HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
