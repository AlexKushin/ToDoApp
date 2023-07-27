package com.shpp.mentoring.springboot.todoapp.dtos;

import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {
    private Long id;
    private String description;
    private Date date;

}
