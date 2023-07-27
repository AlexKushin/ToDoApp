package com.shpp.mentoring.springboot.todoapp.events;

import com.shpp.mentoring.springboot.todoapp.dtos.ToDoDTO;
import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import org.springframework.context.ApplicationEvent;

public class ToDoTransitionedEvent extends ApplicationEvent {
    private final ToDoDTO toDoDTO;
    private final TodoState todoState;

    public ToDoTransitionedEvent(Object source, TodoState todoState, ToDoDTO toDoDTO) {
        super(source);
        this.toDoDTO = toDoDTO;
        this.todoState=todoState;

    }

    public ToDoDTO getToDoDTO() {
        return toDoDTO;
    }

    public TodoState getToDoState() {
        return todoState;
    }

}
