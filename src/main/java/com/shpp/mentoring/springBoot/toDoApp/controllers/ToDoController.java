package com.shpp.mentoring.springBoot.toDoApp.controllers;

import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import com.shpp.mentoring.springBoot.toDoApp.service.ToDoTransitionalService;
import com.shpp.mentoring.springBoot.toDoApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ToDoController {
    private final UserService userService;
    private final ToDoTransitionalService toDoTransitionalService;


    @PutMapping("info/todos/{todo_id}")
    public ResponseEntity<?> changeTodoState(@PathVariable Long todo_id, @RequestParam String transition){

        return new ResponseEntity<>(toDoTransitionalService.transition(todo_id, transition), HttpStatus.CREATED);
    }

}

