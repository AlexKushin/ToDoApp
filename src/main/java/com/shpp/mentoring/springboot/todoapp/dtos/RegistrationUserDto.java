package com.shpp.mentoring.springboot.todoapp.dtos;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationUserDto {
    @NotEmpty
    @Length(min = 5, max = 15)
    private String username;
    @Length(min = 8, max = 20)
    @Pattern(regexp = "([0-9.]*)")
    private String password;
    private String confirmPassword;
    @Email
    @NotEmpty
    private String email;

}
