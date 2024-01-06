package org.home.todo.services;

import org.home.todo.models.Task;
import org.home.todo.models.User;
import org.home.todo.repositories.TaskRepository;
import org.home.todo.security.UserDetails;
import org.home.todo.util.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Service
@Transactional(readOnly = true)
public class TasksService {

    private final TaskRepository taskRepository;

    @Autowired
    public TasksService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void addTask(Task task) {
        task.setUser(getAuthenticatedUser());
        taskRepository.save(task);
    }

    public Task getTask(int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent() && taskOptional.get().getUser().equals(getAuthenticatedUser()))
            return taskOptional.get();
        else throw new TaskNotFoundException("task not found");
    }

    public List<Task> getAllUsersTasks() {
        return taskRepository.findByUser(getAuthenticatedUser());
    }

    @Transactional
    public void updateTask(int id, Task task) {
        Optional<Task> taskToUpdate = taskRepository.findById(id);
        if (taskToUpdate.isPresent() && taskToUpdate.get().getUser().equals(getAuthenticatedUser())) {
            task.setId(id);
            task.setUser(getAuthenticatedUser());
            task.setAddons(taskToUpdate.get().getAddons());
            taskRepository.save(task);
        } else throw new TaskNotFoundException("task not found");
    }

    @Transactional
    public void deleteTask(int id) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUser(id, getAuthenticatedUser());
        if (!taskOptional.isPresent()) throw new TaskNotFoundException("task not found");
        taskRepository.delete(taskOptional.get());
    }

    @Transactional
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUser();
    }
}
