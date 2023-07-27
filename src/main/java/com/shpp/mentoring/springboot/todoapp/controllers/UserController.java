package com.shpp.mentoring.springboot.todoapp.controllers;

import com.shpp.mentoring.springboot.todoapp.dtos.RegistrationUserDto;
import com.shpp.mentoring.springboot.todoapp.dtos.UserDTO;
import com.shpp.mentoring.springboot.todoapp.entities.Role;
import com.shpp.mentoring.springboot.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/current") //get user info
    public ResponseEntity<UserDTO> getUser(Principal principal){
        return new ResponseEntity<>(userService.getDtoByUsername(principal.getName()), HttpStatus.OK);
    }

   /* @PutMapping("/users/current/{username}") //change user info
    public ResponseEntity<UserDTO>putUser(@PathVariable String username, @RequestBody UserDTO userDTO){
        return userService.changeUserData(username, userDTO);
    }

    */



    @PostMapping("/registration") //add new user
    public ResponseEntity<UserDTO> addNewUser(@RequestBody RegistrationUserDto userDTO){
        return new ResponseEntity<>(userService.createNewUser(userDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/current") //delete user
    public ResponseEntity<UserDTO>deleteCurrentUser(Principal principal){
        return new ResponseEntity<>(userService.deleteUser(principal.getName()), HttpStatus.OK);
    }


    @GetMapping("/admin/users") // get list of users
    public ResponseEntity<Iterable<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/admin/users/{username}") // get user info by username
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.getDtoByUsername(username), HttpStatus.OK);
    }


    /*@PutMapping("admin/users/{username}") //change user info by admin
    public ResponseEntity<UserDTO>putUserByAdmin(@PathVariable String username, @RequestBody UserDTO userDTO){
        return userService.changeUserData(username, userDTO);
    }

     */


    @DeleteMapping("admin/users/{username}") //delete user by admin
    public ResponseEntity<UserDTO>deleteUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }
    @GetMapping("admin/users/{username}/roles")
    public ResponseEntity<Iterable<Role>> getUserTodoRoles(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserRoles(username));

    }
}
