package com.example.todo.mapper;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;

public class TaskMapper {

    public static Task mapToTask(TaskDto taskDto) {
        return new Task(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getDueDate(),
                taskDto.isCompleted()
        );
    }

    public static TaskDto mapToTaskDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted()
        );
    }
}
