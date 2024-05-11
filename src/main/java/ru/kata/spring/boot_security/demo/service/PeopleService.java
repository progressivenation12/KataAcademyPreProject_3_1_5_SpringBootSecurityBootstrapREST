package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Person;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PeopleService {
    List<Person> getUsersList();

    Optional<Person> getUserByUsername(String userName);

    Person getUserByID(int id);

    boolean isEmailUnique(String email, int userId);

    @Transactional
    void updateUser(int id, Person updateUser);

    @Transactional
    void deleteUser(int id);
}
