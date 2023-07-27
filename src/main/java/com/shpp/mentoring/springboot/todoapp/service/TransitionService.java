package com.shpp.mentoring.springboot.todoapp.service;

import java.util.List;

public interface TransitionService <T> {

    T transition(Long id, String transition);

    List<String> getAllowedTransitions(Long id);
}
