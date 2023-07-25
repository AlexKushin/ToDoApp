package com.shpp.mentoring.springBoot.toDoApp.repositiries;

import com.shpp.mentoring.springBoot.toDoApp.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
