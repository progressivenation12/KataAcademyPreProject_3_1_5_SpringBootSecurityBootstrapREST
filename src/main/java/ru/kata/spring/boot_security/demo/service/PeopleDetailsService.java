package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;

import java.util.Optional;

@Service
public class PeopleDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    public PeopleDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleRepository.findByUserName(username);

        if (person != null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }

        return person;
    }
}
