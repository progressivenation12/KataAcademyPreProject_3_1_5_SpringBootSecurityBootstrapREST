package ru.kata.spring.boot_security.demo.models;

import ru.kata.spring.boot_security.demo.dto.PersonDTO;

import java.util.List;

public class AdminResponse {
    private List<PersonDTO> people;
    private List<Role> roles;

    public AdminResponse(List<PersonDTO> people, List<Role> roles) {
        this.people = people;
        this.roles = roles;
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
