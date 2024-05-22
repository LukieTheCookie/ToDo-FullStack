package com.example.todo.service.impl;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;
import com.example.todo.mapper.TaskMapper;
import com.example.todo.repository.TaskRepository;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task findTaskById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskMapper.mapToTask(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(savedTask);
    }

    @Override
    public TaskDto getTaskById(long id) {
        return TaskMapper.mapToTaskDto(findTaskById(id));
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto updateTask(long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            throw new RuntimeException("Task not found");
        }
        Task task = optionalTask.get();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setCompleted(taskDto.isCompleted());
        task.setDueDate(taskDto.getDueDate());
        Task updatedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDto(updatedTask);
    }

    @Override
    public void deleteTask(long id) {
        taskRepository.deleteById(findTaskById(id).getId());
    }

    @Override
    public TaskDto updateCompletedStatus(long id, boolean isCompleted) {
        Optional<Task> taskToUpdate = taskRepository.findById(id);
        if (!taskToUpdate.isPresent()) {
            throw new RuntimeException("Task not found");
        } else {
            Task task = taskToUpdate.get();
            task.setCompleted(isCompleted);
            return TaskMapper.mapToTaskDto(taskRepository.save(task));
        }
    }
}
