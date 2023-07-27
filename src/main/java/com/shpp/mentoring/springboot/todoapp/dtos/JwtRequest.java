package com.shpp.mentoring.springboot.todoapp.dtos;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;
}
