package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Person;

import java.util.List;

public interface PeopleService {
    void createNewUser(Person person);
    List<Person> getUsersList();

    Person getUserByID(int id);

    boolean isEmailUnique(String email, int userId);

    void updateUser(int id, Person updateUser);

    void deleteUser(int id);
}
