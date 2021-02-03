package com.zakharov.restful.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private int age;
    private float growth;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birth_date;
    private boolean married;

    public Person() { }

    public Person(String name, String surname, int age, float growth, Date birth_date, boolean married) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.growth = growth;
        this.birth_date = birth_date;
        this.married = married;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getGrowth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birthDate) {
        this.birth_date = birthDate;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }
}


