package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.models.Person;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class RestUserController {
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public RestUserController(UserDetailsService userDetailsService, ModelMapper modelMapper) {
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/info")
    public ResponseEntity<PersonDTO> showUserInfo(Principal principal) {
        Person person = (Person) userDetailsService.loadUserByUsername(principal.getName());
        PersonDTO currentUserDTO = converteToPersonDTO(person);

        return new ResponseEntity<>(currentUserDTO, HttpStatus.OK);
    }

    private PersonDTO converteToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
