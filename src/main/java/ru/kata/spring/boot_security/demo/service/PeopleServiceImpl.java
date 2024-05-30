package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return peopleRepository.findById(id);
    }

    @Override
    public Person getUserByUsername(String userName){
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
        updatePerson.setId(id);
        updatePerson.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        peopleRepository.save(updatePerson);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        peopleRepository.deleteById(id);
    }

}
