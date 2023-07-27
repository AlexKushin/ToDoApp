package com.shpp.mentoring.springboot.todoapp.dtos;

import com.shpp.mentoring.springboot.todoapp.entities.Role;
import com.shpp.mentoring.springboot.todoapp.entities.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String username;
    private String email;





}
