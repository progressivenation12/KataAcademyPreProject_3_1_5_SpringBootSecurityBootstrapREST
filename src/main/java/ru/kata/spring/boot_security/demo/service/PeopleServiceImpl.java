package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.List;
import java.util.Set;

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
        return peopleRepository.findAllWithRoles();
    }

    @Override
    public Person getUserByID(int id) {
        return peopleRepository.findById(id);
    }

//    @Override
//    public boolean isEmailUnique(String email, int userId) {
//        Person personWithSameEmail = peopleRepository.findByEmail(email);
//
//        return personWithSameEmail == null || personWithSameEmail.getId() == userId;
//    }

    @Transactional
    @Override
    public void createNewUser(Person person, Set<Role> roles) {
        person.setPassword(person.getPassword());
        person.setRoleSet(roles);

        peopleRepository.save(person);
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
