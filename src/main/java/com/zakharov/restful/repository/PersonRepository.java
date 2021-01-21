package com.zakharov.restful.repository;

import com.zakharov.restful.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> { }