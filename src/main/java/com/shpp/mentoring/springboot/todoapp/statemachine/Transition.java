package com.shpp.mentoring.springboot.todoapp.statemachine;

public interface Transition <T>{
    String getName();

    TodoState getTargetState();

    //This should do required pre processing
    void applyProcessing(T order);
}
