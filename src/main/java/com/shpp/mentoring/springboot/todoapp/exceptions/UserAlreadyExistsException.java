package com.shpp.mentoring.springboot.todoapp.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Person assigned with ipn = " + username + " has already existed in repository.");
    }
}
