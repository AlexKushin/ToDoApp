package com.shpp.mentoring.springBoot.toDoApp.converter;

import com.shpp.mentoring.springBoot.toDoApp.dtos.ToDoDTO;
import com.shpp.mentoring.springBoot.toDoApp.entities.ToDo;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ToDoConverter {
    public static ToDoDTO convertEntityToDto(ToDo todo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(todo, ToDoDTO.class);
    }

    public static ToDo convertDtoToEntity(ToDoDTO personDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(personDTO, ToDo.class);
    }

    public static List<ToDoDTO> convertEntityListToDtoList(List<ToDo> personEntitiesList) {
        List<ToDoDTO> personDTOList = new ArrayList<>();
        if (personEntitiesList.iterator().hasNext()) {
            for (ToDo todo : personEntitiesList) {
                personDTOList.add(ToDoConverter.convertEntityToDto(todo));
            }
        }
        return personDTOList;
    }
}
