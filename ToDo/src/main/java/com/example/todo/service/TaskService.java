package com.example.todo.service;

import com.example.todo.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    TaskDto getTaskById(long id);
    List<TaskDto> getAllTasks();
    TaskDto updateTask(long id, TaskDto taskDto);
    void deleteTask(long id);
    TaskDto updateCompletedStatus(long id, boolean isCompleted);
}
