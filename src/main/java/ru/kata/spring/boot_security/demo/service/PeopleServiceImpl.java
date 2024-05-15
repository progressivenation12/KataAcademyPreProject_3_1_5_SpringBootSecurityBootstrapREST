package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.List;

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
    public List<Person> getUsersList() {
        return peopleRepository.findAllWithRoles();
    }

    @Override
    public Person getUserByID(int id) {
        return peopleRepository.findById(id);
    }

    @Transactional
    @Override
    public void createNewUser(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        System.out.println("createNewUser" + person);
        peopleRepository.save(person);
    }

    @Transactional
    @Override
    public void updateUser(int id, Person personUpdate) {
        personUpdate.setId(id);
        personUpdate.setPassword(passwordEncoder.encode(personUpdate.getPassword()));
        peopleRepository.save(personUpdate);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        peopleRepository.deleteById(id);
    }
}
