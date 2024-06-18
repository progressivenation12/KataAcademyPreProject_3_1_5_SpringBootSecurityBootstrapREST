package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchPersonException;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl implements PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<Person> getAllUsers() {
        return peopleRepository.findAllWithRoles();
    }

    @Override
    public Person getUserByID(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        if (foundPerson.isEmpty()) {
            throw new NoSuchPersonException("Пользователь с ID = " + id + " не найден в базе данных.");
        }

        return foundPerson.orElseThrow();
    }

    @Override
    public Person getUserByUsername(String userName) {
        return peopleRepository.findByUserName(userName);
    }

    @Transactional
    @Override
    public void addNewUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    @Transactional
    @Override
    public void updateUser(int id, Person updatePerson) {
        Optional<Person> existingPersonOptional = peopleRepository.findById(id);

        if (existingPersonOptional.isEmpty()) {
            throw new NoSuchPersonException("Пользователь с ID = " + id + " не найден в базе данных.");
        }

        Person existingPerson = existingPersonOptional.get();

        updatePerson.setId(id);

        if (updatePerson.getPassword() != null
            && !updatePerson.getPassword().isEmpty()
            && !updatePerson.getPassword().equals(existingPerson.getPassword())) {
            updatePerson.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        } else {
            updatePerson.setPassword(existingPerson.getPassword());
        }

        peopleRepository.save(updatePerson);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        Optional<Person> deletePersson = peopleRepository.findById(id);

        if (deletePersson.isEmpty()) {
            throw new NoSuchPersonException("Пользователь с ID = " + id + " не найден в базе данных.");
        }

        peopleRepository.deleteById(id);
    }

}
