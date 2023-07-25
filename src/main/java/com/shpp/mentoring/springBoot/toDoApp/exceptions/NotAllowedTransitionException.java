package com.shpp.mentoring.springBoot.toDoApp.exceptions;

public class NotAllowedTransitionException extends RuntimeException {
    public  NotAllowedTransitionException(String message) {
        super(message);
    }
}
