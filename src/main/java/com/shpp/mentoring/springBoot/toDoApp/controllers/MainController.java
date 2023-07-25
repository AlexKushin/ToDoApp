package com.shpp.mentoring.springBoot.toDoApp.controllers;

import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import com.shpp.mentoring.springBoot.toDoApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "unsecured data";
    }
    @GetMapping("/secured")
    public String securedData(){
        return "secured data";

    }
    @GetMapping("/admin")
    public String adminData(){
        return "admin data";

    }
    @GetMapping("/info")
    public String userData(Principal principal){
        return principal.getName();

    }
    @GetMapping("/info/todos")
    public ResponseEntity<?> getUserTodoList( Principal principal) {
        return ResponseEntity.ok(userService.findUserTodos(principal.getName()));

    }
    @GetMapping("/info/roles")
    public ResponseEntity<?> getUserTodoRoles( Principal principal) {
        return ResponseEntity.ok(userService.findUserRoles(principal.getName()));

    }
    @PostMapping("info/todos/add")
    public ResponseEntity<?>addNewTodo(@RequestBody ToDo toDo){
        return new ResponseEntity<>(userService.addNewTodo(toDo), HttpStatus.CREATED);
    }

}

