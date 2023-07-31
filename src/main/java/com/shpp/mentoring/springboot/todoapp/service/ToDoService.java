package com.shpp.mentoring.springboot.todoapp.service;

import com.shpp.mentoring.springboot.todoapp.converter.ToDoConverter;
import com.shpp.mentoring.springboot.todoapp.dtos.ToDoDTO;
import com.shpp.mentoring.springboot.todoapp.entities.ToDo;
import com.shpp.mentoring.springboot.todoapp.entities.User;
import com.shpp.mentoring.springboot.todoapp.exceptions.EntityNotFoundException;
import com.shpp.mentoring.springboot.todoapp.exceptions.ToDoNotExistException;
import com.shpp.mentoring.springboot.todoapp.repositiries.ToDoRepository;
import com.shpp.mentoring.springboot.todoapp.statemachine.TodoState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoService {
    private final UserService userService;
    private final ToDoRepository toDoRepository;

    public Iterable<ToDoDTO> getAllToDo() {
        return ToDoConverter.convertEntityListToDtoList(toDoRepository.findAll());
    }

    public ToDoDTO getToDoById(Long id) {
        return getToDoByIdFromList(getAllToDo(), id);

    }

    public List<ToDoDTO> getUserTodosDto(String username) {
        User user = userService.getByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return ToDoConverter.convertEntityListToDtoList(user.getTodos());
    }

    public List<ToDo> getUserTodos(String username) {
        User user = userService.getByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return user.getTodos();
    }

    public ToDoDTO getUsersTodoByToDoId(String username, Long id) {
        return getToDoByIdFromList(getUserTodosDto(username), id);
    }

    public ToDoDTO addNewTodo(ToDoDTO toDoDTO, String username) {
        ToDo newToDo = new ToDo();
        newToDo.setDescription(toDoDTO.getDescription());
        newToDo.setDate(toDoDTO.getDate());
        newToDo.setExpirationDate(toDoDTO.getExpirationDate());
        newToDo.setTodoState(TodoState.PLANNED);
        List<ToDo> todoList = getUserTodos(username);
        todoList.add(newToDo);
        userService.getByUsername(username).get().setTodos(todoList);
        toDoRepository.save(newToDo);
        return ToDoConverter.convertEntityToDto(newToDo);
    }

    public ToDoDTO deleteTodo(Long id, String username) {

        Optional<ToDo> todo = toDoRepository.findById(id);
        if (todo.isEmpty()) {
            log.error("No any ToDo by id = {}", id);
            throw new EntityNotFoundException("No any ToDo by id = " + id);
        }
        log.info("Try to delete ToDo by id {} from repository", id);
        List<ToDo> todoList = getUserTodos(username);
        todoList.removeIf(toDo -> Objects.equals(toDo.getId(), id));
        userService.getByUsername(username).get().setTodos(todoList);
        toDoRepository.deleteById(id);
        log.info("ToDo assigned by id {} was deleted from repository", id);

        return ToDoConverter.convertEntityToDto(todo.get());
    }


    private ToDoDTO getToDoByIdFromList(Iterable<ToDoDTO> toDoDTOList, Long id) {
        for (ToDoDTO todo : toDoDTOList) {
            if (Objects.equals(todo.getId(), id)) {
                return todo;
            }
        }
        throw new ToDoNotExistException("ToDo with id {} does not exist" + id);
    }


}
