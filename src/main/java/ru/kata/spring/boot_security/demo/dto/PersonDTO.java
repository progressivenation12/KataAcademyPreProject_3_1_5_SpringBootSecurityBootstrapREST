package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class PersonDTO {

    private int id;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 2, max = 25, message = "Имя и фамилия должны содержать от 2 до 25 символов!")
    private String userName;

    @Transient
    private String oldUserName;

    @NotNull(message = "Поле не должно быть пустым!")
    @Min(value = 0, message = "Возраст не должен быть меньше 0!")
    @Max(value = 150, message = "Возраст не должен превышать 155 лет!")
    private Integer age;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Email(message = "Электронная почта должна быть действительной!")
    private String email;

    @NotBlank(message = "Поле не должно быть пустым!")
    private String password;

    @Size(min = 1, message = "Пожалуйста, выберите хотя бы одну роль.")
    private Set<Role> roleSet = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldUserName() {
        return oldUserName;
    }

    public void setOldUserName(String oldUserName) {
        this.oldUserName = oldUserName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
               "id=" + id +
               ", userName='" + userName + '\'' +
               ", oldUserName='" + oldUserName + '\'' +
               ", age=" + age +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", roleSet=" + roleSet +
               '}';
    }
}
