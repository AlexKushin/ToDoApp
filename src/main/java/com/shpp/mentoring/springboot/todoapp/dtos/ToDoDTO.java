package com.shpp.mentoring.springboot.todoapp.dtos;

import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {

    private Long id;
    @Length(min = 5, max = 100)
    private String description;
    @PastOrPresent
    private Date date;
    @Future
    private Date expirationDate;

    private TodoState todoState;

}
