package com.zakharov.restful.service;

import com.zakharov.restful.exception.EmptyDBException;
import com.zakharov.restful.exception.EntityNotFoundException;
import com.zakharov.restful.model.Person;
import com.zakharov.restful.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final  PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void create(Person person) {
        personRepository.save(person);
    }

    @Override
    public List<Person> readAll() throws EmptyDBException {
        final List<Person> people = personRepository.findAll();
        if (!people.isEmpty()) {
            return people;
        }
        else throw new EmptyDBException();
    }

    @Override
    public Person readById(int id) throws EntityNotFoundException {
        if (personRepository.existsById(id)) {
            return personRepository.getOne(id);
        }
        else throw new EntityNotFoundException(id);
    }

    @Override
    public void update(Person person, int id) throws EntityNotFoundException {
        if (personRepository.existsById(id)) {
            person.setId(id);
            personRepository.save(person);
        }
        else throw new EntityNotFoundException(id);
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }
        else throw new EntityNotFoundException(id);
    }
}