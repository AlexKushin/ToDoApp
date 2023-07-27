package com.shpp.mentoring.springboot.todoapp.statemachine;

import com.shpp.mentoring.springboot.todoapp.dtos.ToDoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Postpone implements Transition<ToDoDTO> {
    public static final String NAME = "postpone";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public TodoState getTargetState() {
        return TodoState.POSTPONED;
    }

    @Override
    public void applyProcessing(ToDoDTO toDoDTO) {
        log.info("Account is transitioning to approved state {}", toDoDTO.getId());
    }
}
