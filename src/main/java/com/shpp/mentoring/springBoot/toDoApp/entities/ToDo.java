package com.shpp.mentoring.springBoot.toDoApp.entities;

import com.shpp.mentoring.springBoot.toDoApp.stateMachine.TodoState;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //можно  uuid/string что угодно

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;

    @Column(name = "todo_state")
    @Enumerated(EnumType.STRING)
    private TodoState todoState;



}
