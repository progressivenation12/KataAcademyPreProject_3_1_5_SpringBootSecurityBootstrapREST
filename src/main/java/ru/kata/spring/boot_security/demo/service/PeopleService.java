package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Person;

import java.util.List;

public interface PeopleService {
    void addNewUser(Person person);
    List<Person> getAllUsers();
    Person getUserByUsername(String userName);
    Person getUserByID(int id);
    void updateUser(int id, Person updatePerson);
    void deleteUser(int id);
}
