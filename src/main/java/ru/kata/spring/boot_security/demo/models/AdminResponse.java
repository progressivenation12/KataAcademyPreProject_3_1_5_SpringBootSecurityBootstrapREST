package ru.kata.spring.boot_security.demo.models;

import ru.kata.spring.boot_security.demo.dto.PersonDTO;

import java.util.List;
import java.util.Set;

public class AdminResponse {
    private Set<PersonDTO> people;
    private Set<Role> roles;

    public AdminResponse(Set<PersonDTO> people, Set<Role> roles) {
        this.people = people;
        this.roles = roles;
    }

    public Set<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(Set<PersonDTO> people) {
        this.people = people;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
