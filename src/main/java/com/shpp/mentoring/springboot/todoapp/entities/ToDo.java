package com.shpp.mentoring.springboot.todoapp.entities;

import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "expirationdate")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date expirationDate;

    @Column(name = "todo_state")
    @Enumerated(EnumType.STRING)
    private TodoState todoState;



}
