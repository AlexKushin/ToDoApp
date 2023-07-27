package com.shpp.mentoring.springboot.todoapp.controllers;

import com.shpp.mentoring.springboot.todoapp.dtos.ToDoDTO;
import com.shpp.mentoring.springboot.todoapp.service.ToDoService;
import com.shpp.mentoring.springboot.todoapp.service.ToDoTransitionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoTransitionalService toDoTransitionalService;
    private final ToDoService toDoService;


    @GetMapping("todos") //get list of user tasks
    public ResponseEntity<Iterable<ToDoDTO>> getUserTodos(Principal principal) {
        return new ResponseEntity<>(toDoService.getUserTodosDto(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("todos/{id}") // get user task by id
    public ResponseEntity<ToDoDTO> getUserTodosById(Principal principal, @PathVariable Long id) {
        return new ResponseEntity<>(toDoService.getUsersTodoByToDoId(principal.getName(), id), HttpStatus.OK);
    }

    @PutMapping("todos/{id}") // change task by id
    public ResponseEntity<ToDoDTO> changeTodoState(@PathVariable Long id, @RequestParam String transition) {
        return new ResponseEntity<>(toDoTransitionalService.transition(id, transition), HttpStatus.CREATED);
    }

    @PostMapping("todos") //create new task
    public ResponseEntity<ToDoDTO> addNewTodo(@RequestBody ToDoDTO toDoDTO, Principal principal) {
        return new ResponseEntity<>(toDoService.addNewTodo(toDoDTO, principal.getName()), HttpStatus.CREATED);
    }

    @DeleteMapping("todos/{id}") //delete task by id
    public ResponseEntity<ToDoDTO> deleteToDo(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(toDoService.deleteTodo(id, principal.getName()), HttpStatus.OK);
    }


    @GetMapping("admin/todos") //get list of user task
    public ResponseEntity<Iterable<ToDoDTO>> getUserTodoList() {
        //return ResponseEntity.ok(toDoService.getAllToDo());
        return new ResponseEntity<>(toDoService.getAllToDo(), HttpStatus.OK);
    }

    @GetMapping("admin/todos/{id}") // get user task by id
    public ResponseEntity<ToDoDTO> getTodosById(@PathVariable Long id) {
        return new ResponseEntity<>(toDoService.getToDoById(id), HttpStatus.OK);
    }
    @GetMapping("admin/todos/") // get user task by id
    public ResponseEntity<Iterable<ToDoDTO>> getTodosByUsername( @RequestParam String username) {
        return new ResponseEntity<>(toDoService.getUserTodosDto(username), HttpStatus.OK);
    }

    @PutMapping("admin/todos/{id}")
    public ResponseEntity<ToDoDTO> changeTodoStateByAdmin(@PathVariable Long id, @RequestParam String transition) {
        return new ResponseEntity<>(toDoTransitionalService.transition(id, transition), HttpStatus.OK);
    }


    @PostMapping("admin/todos") //create new task
    public ResponseEntity<ToDoDTO> addNewTodoByAdmin(@RequestBody ToDoDTO toDoDTO, @RequestParam String username) {
        return new ResponseEntity<>(toDoService.addNewTodo(toDoDTO, username), HttpStatus.CREATED);
    }

    @DeleteMapping("admin/todos/{id}") //delete task by id
    public ResponseEntity<ToDoDTO> deleteToDoByAdmin(@PathVariable Long id, @RequestParam String username) {
        return new ResponseEntity<>(toDoService.deleteTodo(id, username), HttpStatus.OK);
    }
}


