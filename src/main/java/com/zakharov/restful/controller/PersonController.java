package com.zakharov.restful.controller;

import com.zakharov.restful.exception.EmptyDBException;
import com.zakharov.restful.exception.EntityNotFoundException;
import com.zakharov.restful.model.Person;
import com.zakharov.restful.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    Logger logger = LoggerFactory.getLogger(PersonController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public ResponseEntity<?> readAll() throws EmptyDBException {
        final List<Person> people = personService.readAll();
        if (!people.isEmpty()) {
            return new ResponseEntity<>(people, HttpStatus.OK);
        }
        else throw new EmptyDBException();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> readById(@PathVariable(name = "id") int id) throws EntityNotFoundException {
        final Person person = personService.readById(id);
        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        else throw new EntityNotFoundException(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @Valid @RequestBody Person person) throws EntityNotFoundException {
        if (personService.update(person, id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else throw new EntityNotFoundException(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) throws EntityNotFoundException {
        if (personService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else throw new EntityNotFoundException(id);
    }
}
