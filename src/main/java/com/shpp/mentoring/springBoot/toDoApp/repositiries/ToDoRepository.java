package com.shpp.mentoring.springBoot.toDoApp.repositiries;

import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo,Long> {


}
