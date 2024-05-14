package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.Person;

@Component
public class PersonValidator implements Validator {
    private final UserDetailsService userDetailsService;

    @Autowired
    public PersonValidator(UserDetailsService userDetailsService) {
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
        }
    }
}
