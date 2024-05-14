package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface PeopleService {
    void createNewUser(Person person, Set<Role> roles);
    List<Person> getUsersList();

    Person getUserByID(int id);

    void updateUser(int id, Person updateUser);

    void deleteUser(int id);
}
