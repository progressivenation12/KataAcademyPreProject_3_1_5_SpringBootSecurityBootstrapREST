package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.service.PeopleService;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public PersonValidator(PeopleService peopleService, UserDetailsService userDetailsService) {
        this.peopleService = peopleService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (userDetailsService.loadUserByUsername(person.getUserName()) != null) {
            errors.rejectValue("userName", "", "Это имя пользователя уже занято!");
        } else if (!peopleService.isEmailUnique(person.getEmail(), person.getId())) {
            errors.rejectValue("email", "", "Пользователь с такой эл. почтой уже зарегистрирован!");
        }
    }
}
