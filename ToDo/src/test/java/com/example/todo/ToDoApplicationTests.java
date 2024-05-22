package com.example.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import com.example.todo.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ToDoApplicationTests {
	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskServiceImpl taskService;

	@Captor
	private ArgumentCaptor<Task> taskCaptor;

	private static final Logger logger = LoggerFactory.getLogger(ToDoApplicationTests.class);

	@Test
	public void testCreateTask() {
		// Arrange
		TaskDto taskDto = new TaskDto();
		taskDto.setTitle("Test Task");
		taskDto.setDescription("Test Description");
		taskDto.setDueDate("2024-05-21");
		taskDto.setCompleted(false);

		Task savedTask = new Task();
		savedTask.setId(1L);
		savedTask.setTitle("Test Task");
		savedTask.setDescription("Test Description");
		savedTask.setDueDate("2024-05-21");
		savedTask.setCompleted(false);

		doReturn(savedTask).when(taskRepository).save(any(Task.class));

		// Act
		TaskDto createdTask = taskService.createTask(taskDto);

		// Assert
		assertNotNull(createdTask);
		assertEquals(1L, createdTask.getId());
		assertEquals("Test Task", createdTask.getTitle());
		assertEquals("Test Description", createdTask.getDescription());
		assertEquals("2024-05-21", createdTask.getDueDate());
		assertFalse(createdTask.isCompleted());

		verify(taskRepository, times(1)).save(taskCaptor.capture());
		Task capturedTask = taskCaptor.getValue();
		assertNull(capturedTask.getId()); // ID should not be set before saving
		assertEquals("Test Task", capturedTask.getTitle());
		assertEquals("Test Description", capturedTask.getDescription());
		assertEquals("2024-05-21", capturedTask.getDueDate());
		assertFalse(capturedTask.isCompleted());

		logger.info("Task created successfully with ID: {}", createdTask.getId());
	}

	@Test
	public void testGetTaskById_TaskNotFound() {
		long taskId = 1L;

		when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			taskService.getTaskById(taskId);
		}, "Task not found");
	}

	@Test
	public void testGetTaskById_Success() {
		// Arrange
		long taskId = 1L;
		Task task = new Task();
		task.setId(taskId);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setDueDate("2024-05-21");
		task.setCompleted(false);

		when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

		// Act
		TaskDto result = taskService.getTaskById(taskId);

		// Assert
		assertNotNull(result);
		assertEquals(task.getId(), result.getId());
		assertEquals(task.getTitle(), result.getTitle());
		assertEquals(task.getDescription(), result.getDescription());
		assertEquals(task.getDueDate(), result.getDueDate());
		assertEquals(task.isCompleted(), result.isCompleted());
	}

//	Unit Tests to complete:
//	Ensure that a task can be retrieved successfully by its ID.
//
//	Check that the number of retrieved tasks matches the number of tasks in the database.
//
//			Test for handling the case when the task ID does not exist.
//	Ensure that a task can be updated successfully with valid input.
//
//	Check that the task is no longer present in the database after deletion.
//
//	Check that the completed status of the task matches the input parameter.
//
//	Test for handling null or empty input parameters.
//	Test for handling invalid task IDs.
//	Test for handling errors that may occur during database operations.

}
