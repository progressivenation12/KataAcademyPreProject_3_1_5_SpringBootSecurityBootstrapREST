package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query(value = "SELECT p FROM Person p JOIN FETCH p.roleSet WHERE p.userName=:username")
    Person findByUserName(@Param("username") String username);

    @Query(value = "SELECT p FROM Person p JOIN FETCH p.roleSet WHERE p.id=:id")
    Optional<Person> findById(@Param("id") int id);

    @Query(value = "SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roleSet")
    List<Person> findAllWithRoles();

//    @Query(value = "SELECT COUNT(p) = 0 FROM Person p WHERE LOWER(p.email) = LOWER(:email) AND p.id <> :userId")
//    Person findByEmail(@Param("email") String email);
}
