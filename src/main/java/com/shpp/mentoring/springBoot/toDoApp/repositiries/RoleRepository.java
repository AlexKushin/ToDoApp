package com.shpp.mentoring.springBoot.toDoApp.repositiries;

import com.shpp.mentoring.springBoot.toDoApp.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(String integer);
}
