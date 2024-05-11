package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl implements PeopleService, UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public List<Person> getUsersList() {
        return peopleRepository.findAll();
    }

    @Override
    public Person getUserByUsername(String userName) {
        return peopleRepository.findByUserName(userName);
    }

    @Override
    public Person getUserByID(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Override
    public boolean isEmailUnique(String email, int userId) {
        Person personWithSameEmail = peopleRepository.findByEmail(email);

        return personWithSameEmail == null || personWithSameEmail.getId() == userId;
    }

    @Transactional
    @Override
    public void updateUser(int id, Person personUpdate) {
        personUpdate.setId(id);
        peopleRepository.save(personUpdate);
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        peopleRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleRepository.findByUserName(username);

        if(person == null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }

        return person;
    }
}
