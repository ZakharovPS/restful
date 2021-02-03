package com.zakharov.restful.service;
import com.zakharov.restful.model.Person;
import java.util.List;

public interface PersonService {
    void create(Person person);
    List<Person> readAll();
    Person readById(int id);
    boolean update(Person person, int id);
    boolean delete(int id);
}
