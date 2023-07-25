package com.shpp.mentoring.springBoot.toDoApp.service;

import com.shpp.mentoring.springBoot.toDoApp.entities.Role;
import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import com.shpp.mentoring.springBoot.toDoApp.stateMachine.TodoState;
import com.shpp.mentoring.springBoot.toDoApp.entities.User;
import com.shpp.mentoring.springBoot.toDoApp.repositiries.RoleRepository;
import com.shpp.mentoring.springBoot.toDoApp.repositiries.ToDoRepository;
import com.shpp.mentoring.springBoot.toDoApp.repositiries.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    // лучше создать сервис под RoleRepository и инжектить
    // его вместо прямого инжекта RoleRepository
    private final RoleRepository roleRepository;

    private final ToDoRepository toDoRepository;


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    //находим в базе юзера, если не находим то бросаем эксепш,
    // если нашли то преобразовываем юзера к типу юзера
    // который понятен для Спринг Секьюрити(имя, пароль, список прав доступа)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
    public Iterable<ToDo> findUserTodos(String username){
        User user = findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return user.getTodos();
    }
    public ToDo getUsersTodoById(String username, Long id){
        User user = findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        List<ToDo> todoList = new ArrayList<>(user.getTodos());
        return todoList.get(Math.toIntExact(id));
    }

    public Iterable<Role> findUserRoles(String username){
        User user = findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return user.getRoles();
    }
    public ToDo addNewTodo(ToDo toDo){
        ToDo newTodo = new ToDo();
        newTodo.setId(toDo.getId());
        newTodo.setDescription(toDo.getDescription());
        newTodo.setDate(toDo.getDate());
        newTodo.setTodoState(TodoState.NOTIFIED);
        toDoRepository.save(toDo);
        return toDo;
    }
    public void createNewUser(User user){
        //TODO  проверка на то существует ли пользователь

        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);

    }
}
