package com.shpp.mentoring.springboot.todoapp.converter;

import com.shpp.mentoring.springboot.todoapp.dtos.UserDTO;
import com.shpp.mentoring.springboot.todoapp.entities.User;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    private UserConverter(){}

    public static UserDTO convertEntityToDto(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    public static User convertDtoToEntity(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }

    public static List<UserDTO> convertEntityListToDtoList(List<User> userEntitiesList) {
        List<UserDTO> userDtoList = new ArrayList<>();
        if (userEntitiesList.iterator().hasNext()) {
            for (User user : userEntitiesList) {
                userDtoList.add(UserConverter.convertEntityToDto(user));
            }
        }
        return userDtoList;
    }

}
