package com.zakharov.restful.repository;

import com.zakharov.restful.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
    MyUser findByLogin(String login);
}
