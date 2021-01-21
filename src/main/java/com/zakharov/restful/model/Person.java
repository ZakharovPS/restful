package com.zakharov.restful.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private int age;
    private float growth;
    @Temporal(TemporalType.DATE)
    private Date birth_date;
    private boolean married;

    public Person() { }

    public Person(int id, String name, String surname, int age, float growth, Date birth_date, boolean married) {
        this.id = id;
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


