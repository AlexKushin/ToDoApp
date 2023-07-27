package com.shpp.mentoring.springboot.todoapp.service;

import com.shpp.mentoring.springboot.todoapp.converter.UserConverter;
import com.shpp.mentoring.springboot.todoapp.dtos.RegistrationUserDto;
import com.shpp.mentoring.springboot.todoapp.dtos.UserDTO;
import com.shpp.mentoring.springboot.todoapp.entities.Role;
import com.shpp.mentoring.springboot.todoapp.entities.User;
import com.shpp.mentoring.springboot.todoapp.exceptions.EntityNotFoundException;
import com.shpp.mentoring.springboot.todoapp.exceptions.UserAlreadyExistsException;
import com.shpp.mentoring.springboot.todoapp.repositiries.RoleRepository;
import com.shpp.mentoring.springboot.todoapp.repositiries.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    // лучше создать сервис под RoleRepository и инжектить
    // его вместо прямого инжекта RoleRepository

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    //находим в базе юзера, если не находим то бросаем эксепш,
    // если нашли то преобразовываем юзера к типу юзера
    // который понятен для Спринг Секьюрити(имя, пароль, список прав доступа)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    public Iterable<UserDTO> getUsers() {
        log.info("List of all Users is returned");
        return UserConverter.convertEntityListToDtoList(userRepository.findAll());
    }

    public Optional<User> getByUsername(String username) {
        if (userRepository.getUserByUsername(username).isEmpty()) {
            log.error("No any User by assigned username = {}", username);
            throw new EntityNotFoundException("Entity is not found, username =" + username);
        }
        return userRepository.findByUsername(username);
    }

    public UserDTO getDtoByUsername(String username) {
        if (userRepository.getUserByUsername(username).isEmpty()) {
            log.error("No any User by assigned username = {}", username);
            throw new EntityNotFoundException("Entity is not found, username =" + username);
        }
        return UserConverter.convertEntityToDto(userRepository.findByUsername(username).get());
    }

    public Optional<User> getByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("No any Person by assigned ipn = {}", userId);
            throw new EntityNotFoundException("Entity is not found, user id =" + userId);
        }
        return userRepository.findById(userId);
    }


    public Iterable<Role> getUserRoles(String username) {
        User user = getByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return user.getRoles();
    }

    public UserDTO createNewUser(RegistrationUserDto userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }
        User newUser = new User();
        newUser.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setEmail(userDTO.getEmail());
        userRepository.save(newUser);
        return new UserDTO(userDTO.getUsername(),userDTO.getEmail());
    }


    public UserDTO deleteUser(String username) {
        Optional<User> user = getByUsername(username);
        if (user.isEmpty()) {
            log.error("No any User by assigned username = {}", username);
            throw new EntityNotFoundException("Entity is not found, username =" + username);
        }
        log.info("Try to delete User by username {} from repository", username);
        userRepository.deleteById(user.get().getId());
        log.info("User assigned by username {} was deleted from repository", username);
        return UserConverter.convertEntityToDto(user.get());
    }


    public ResponseEntity<UserDTO> changeUserData(String username, UserDTO userDTO) {
        User user = UserConverter.convertDtoToEntity(userDTO);
        log.info("Try to put Person to repository, Person: first name = {}, last name = {}, ipn = {}",
                user.getUsername(), user.getId(), user.getEmail());


        return (userRepository.findByUsername(username).isPresent()) ? new ResponseEntity<>(UserConverter
                .convertEntityToDto(userRepository.save(user)), HttpStatus.OK)
                : new ResponseEntity<>(UserConverter
                .convertEntityToDto(userRepository.save(user)), HttpStatus.CREATED);
    }

}
