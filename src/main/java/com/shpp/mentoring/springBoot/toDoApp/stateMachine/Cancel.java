package com.shpp.mentoring.springBoot.toDoApp.stateMachine;

import com.shpp.mentoring.springBoot.toDoApp.dtos.ToDoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Cancel implements Transition<ToDoDTO> {
    public static final String NAME = "cancel";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public TodoState getTargetState() {
        return TodoState.CANCELED;
    }

    @Override
    public void applyProcessing(ToDoDTO toDoDTO) {
        log.info("Account is transitioning to approved state {}", toDoDTO.getId());
    }
}
