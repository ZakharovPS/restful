package com.zakharov.restful.service;
import com.zakharov.restful.exception.EmptyDBException;
import com.zakharov.restful.exception.EntityNotFoundException;
import com.zakharov.restful.model.Person;
import java.util.List;

public interface PersonService {
    void create(Person person);
    List<Person> readAll() throws EmptyDBException;
    Person readById(int id) throws EntityNotFoundException;
    void update(Person person, int id) throws EntityNotFoundException;
    void delete(int id) throws EntityNotFoundException;
}
