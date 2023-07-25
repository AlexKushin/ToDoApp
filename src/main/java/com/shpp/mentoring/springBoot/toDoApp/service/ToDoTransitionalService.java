package com.shpp.mentoring.springBoot.toDoApp.service;

import com.shpp.mentoring.springBoot.toDoApp.converter.ToDoConverter;
import com.shpp.mentoring.springBoot.toDoApp.dtos.ToDoDTO;
import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import com.shpp.mentoring.springBoot.toDoApp.events.ToDoTransitionedEvent;
import com.shpp.mentoring.springBoot.toDoApp.exceptions.NotAllowedTransitionException;
import com.shpp.mentoring.springBoot.toDoApp.repositiries.ToDoRepository;
import com.shpp.mentoring.springBoot.toDoApp.stateMachine.TodoState;
import com.shpp.mentoring.springBoot.toDoApp.stateMachine.Transition;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ToDoTransitionalService implements TransitionService<ToDoDTO>{

    private Map<String, Transition<ToDoDTO>> transitionsMap;
    private final List<Transition<ToDoDTO>> transitions;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ToDoRepository toDoRepository;


    /**
     * Returns a list of transitions allowed for a particular order identified by the id
     *
     * @param id: the id  of the order
     * return: list of  transitions allowed
     */
    @Override
    public List<String> getAllowedTransitions(Long id) {
        ToDo todo = toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown account: " + id));
        return todo.getTodoState().getTransitions();
    }
    /**
     * Transitions the order from the current state to the target state
     *
     * @param id:         the id of the account
     * @param transition: the status to transition to
     * return: the order details
     */
    @Override
    public ToDoDTO transition(Long id, String transition) {
        Transition<ToDoDTO> accountTransition = transitionsMap.get(transition.toLowerCase());
        if (accountTransition == null) {
            throw new IllegalArgumentException("Unknown transition: " + transition);
        }
        return toDoRepository.findById(id)
                .map(todo -> {
                    checkAllowed(accountTransition, todo.getTodoState());
                    accountTransition.applyProcessing(ToDoConverter.convertEntityToDto(todo));
                    return updateStatus(todo, accountTransition.getTargetState());
                })
                .map(u -> ToDoConverter.convertEntityToDto((ToDo) u))
                .orElseThrow(() -> new IllegalArgumentException("Unknown order: " + id));
    }



    @PostConstruct
    private void initTransitions() {
        Map<String, Transition<ToDoDTO>> transitionHashMap = new HashMap<>();
        for (Transition<ToDoDTO> todoTransition : transitions) {
            if (transitionHashMap.containsKey(todoTransition.getName())) {
                throw new IllegalStateException("Duplicate transition :" + todoTransition.getName());
            }
            transitionHashMap.put(todoTransition.getName(), todoTransition);
        }
        this.transitionsMap = transitionHashMap;
    }

    private Object updateStatus(ToDo todo, TodoState targetState) {
        TodoState existingStatus = todo.getTodoState();
        todo.setTodoState(targetState);
        ToDo updated = toDoRepository.save(todo);

        var event = new ToDoTransitionedEvent(this, existingStatus, ToDoConverter.convertEntityToDto(updated));
        applicationEventPublisher.publishEvent(event);
        return updated;
    }

    private void checkAllowed(Transition<ToDoDTO> accountTransition, TodoState state) {
        Set<TodoState> allowedSourceStatuses = Stream.of(TodoState.values())
                .filter(s -> s.getTransitions().contains(accountTransition.getName()))
                .collect(Collectors.toSet());
        if (!allowedSourceStatuses.contains(state)) {
            throw new NotAllowedTransitionException("The transition from the current state " + state.name() + "  to the target state "
                    + accountTransition.getTargetState().name() + "  is not allowed!");
        }
    }
}
