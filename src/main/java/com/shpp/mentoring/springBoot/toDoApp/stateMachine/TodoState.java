package com.shpp.mentoring.springBoot.toDoApp.stateMachine;

import java.util.Arrays;
import java.util.List;

public enum TodoState {
    PLANNED(Start.NAME, Postpone.NAME, Cancel.NAME),
    WORK_IN_PROGRESS(Notify.NAME, Sign.NAME, Cancel.NAME ),
    POSTPONED(Notify.NAME, Sign.NAME, Cancel.NAME),
    NOTIFIED(Finish.NAME, Cancel.NAME),
    SIGNED(Finish.NAME, Cancel.NAME),
    DONE(),
    CANCELED();
    private final List<String> transitions;
    //the enum accepts a var arg argument that means for each step we can pass 0 or more transitions
    TodoState (String... transitions) {
        this.transitions = Arrays.asList(transitions);
    }
    public List<String> getTransitions() {
        return transitions;
    }

}


