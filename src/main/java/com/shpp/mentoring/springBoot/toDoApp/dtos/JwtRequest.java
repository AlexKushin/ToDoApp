package com.shpp.mentoring.springBoot.toDoApp.dtos;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;
}
