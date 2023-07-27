package com.shpp.mentoring.springboot.todoapp.exceptions;

public class ToDoNotExistException extends RuntimeException{
    public ToDoNotExistException(String message){
        super(message);
    }
}
