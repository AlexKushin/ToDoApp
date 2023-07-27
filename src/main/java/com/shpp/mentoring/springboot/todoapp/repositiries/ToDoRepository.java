package com.shpp.mentoring.springboot.todoapp.repositiries;

import com.shpp.mentoring.springboot.todoapp.entities.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo,Long> {


}
