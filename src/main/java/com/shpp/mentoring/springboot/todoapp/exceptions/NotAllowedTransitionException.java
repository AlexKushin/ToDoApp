package com.shpp.mentoring.springboot.todoapp.exceptions;

public class NotAllowedTransitionException extends RuntimeException {
    public  NotAllowedTransitionException(String message) {
        super(message);
    }
}
