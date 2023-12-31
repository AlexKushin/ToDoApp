package com.shpp.mentoring.springboot.todoapp.service;

import com.shpp.mentoring.springboot.todoapp.converter.ToDoConverter;
import com.shpp.mentoring.springboot.todoapp.dtos.ToDoDTO;
import com.shpp.mentoring.springboot.todoapp.entities.ToDo;
import com.shpp.mentoring.springboot.todoapp.events.ToDoTransitionedEvent;
import com.shpp.mentoring.springboot.todoapp.exceptions.NotAllowedTransitionException;
import com.shpp.mentoring.springboot.todoapp.repositiries.ToDoRepository;
import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import com.shpp.mentoring.springboot.todoapp.statemachine.Transition;
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


    @Override
    public List<String> getAllowedTransitions(Long id) {
        ToDo todo = toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown account: " + id));
        return todo.getTodoState().getTransitions();
    }

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
