package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchPersonException;
import ru.kata.spring.boot_security.demo.exception_handling.PersonIncorrectData;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.service.PeopleService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.PersonValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final ModelMapper modelMapper;
    private final PeopleService peopleService;
    private final RoleService roleService;
    private final PersonValidator personValidator;
    private final UserDetailsService userDetailsService;

    @Autowired
    public RestAdminController(ModelMapper modelMapper, PeopleService peopleService, RoleService roleService, PersonValidator personValidator, UserDetailsService userDetailsService) {
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
        this.roleService = roleService;
        this.personValidator = personValidator;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/accountInfo")
    public ResponseEntity<PersonDTO> showUserInfo(Principal principal) {
        Person person = (Person) userDetailsService.loadUserByUsername(principal.getName());
        PersonDTO currentUserDTO = convertToPersonDTO(person);

        return new ResponseEntity<>(currentUserDTO, HttpStatus.OK);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<PersonDTO>> getAllUsers() {
        List<PersonDTO> peopleDTO = peopleService.getAllUsers().stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(peopleDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("userId") int userId) {
        PersonDTO personDTO = convertToPersonDTO(peopleService.getUserByID(userId));

        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getAllRoles() {
        Set<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping("/new-user")
    public ResponseEntity<?> addNewUser(@RequestBody @Valid PersonDTO newPersonDTO, BindingResult bindingResult) {
        personValidator.validate(newPersonDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        peopleService.addNewUser(convertToPerson(newPersonDTO));

        return new ResponseEntity<>(newPersonDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        personDTO.setOldUserName(peopleService.getUserByID(personDTO.getId()).getUserName());
        personValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            System.out.println(errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        peopleService.updateUser(personDTO.getId(), convertToPerson(personDTO));

        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PersonDTO> deleteUser(@PathVariable("id") int id) {
        peopleService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<PersonIncorrectData> handleException(NoSuchPersonException exception) {
        PersonIncorrectData data = new PersonIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

}
