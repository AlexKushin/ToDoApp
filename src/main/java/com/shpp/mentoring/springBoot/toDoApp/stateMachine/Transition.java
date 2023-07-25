package com.shpp.mentoring.springBoot.toDoApp.stateMachine;

public interface Transition <T>{
    String getName();

    TodoState getTargetState();

    //This should do required pre processing
    void applyProcessing(T order);
}
