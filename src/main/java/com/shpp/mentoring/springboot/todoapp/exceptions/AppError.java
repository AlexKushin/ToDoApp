package com.shpp.mentoring.springboot.todoapp.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private int status;
    private String message;
    private Date timstamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timstamp = new Date();
    }
}
