package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Person;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query(value = "SELECT p FROM Person p JOIN FETCH p.roleSet WHERE p.userName=:username")
    Person findByUserName(@Param("username") String username);

    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roleSet")
    List<Person> findAllWithRoles();

    Person findByEmail(String email);
}
