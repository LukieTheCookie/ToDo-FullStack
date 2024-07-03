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

import java.util.Arrays;
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

		TaskDto createdTask = taskService.createTask(taskDto);

		assertNotNull(createdTask);
		assertEquals(1L, createdTask.getId());
		assertEquals("Test Task", createdTask.getTitle());
		assertEquals("Test Description", createdTask.getDescription());
		assertEquals("2024-05-21", createdTask.getDueDate());
		assertFalse(createdTask.isCompleted());

		verify(taskRepository, times(1)).save(taskCaptor.capture());
		Task capturedTask = taskCaptor.getValue();
		assertNull(capturedTask.getId());
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
		long taskId = 1L;
		Task task = new Task();
		task.setId(taskId);
		task.setTitle("Test Task");
		task.setDescription("Test Description");
		task.setDueDate("2024-05-21");
		task.setCompleted(false);

		when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

		TaskDto result = taskService.getTaskById(taskId);

		assertNotNull(result);
		assertEquals(task.getId(), result.getId());
		assertEquals(task.getTitle(), result.getTitle());
		assertEquals(task.getDescription(), result.getDescription());
		assertEquals(task.getDueDate(), result.getDueDate());
		assertEquals(task.isCompleted(), result.isCompleted());
	}

	@Test
	public void testGetAllTasks_Success() {
		Task task1 = new Task();
		task1.setId(1L);
		task1.setTitle("Task 1");
		task1.setDescription("Description 1");
		task1.setDueDate("2024-05-21");
		task1.setCompleted(false);

		Task task2 = new Task();
		task2.setId(2L);
		task2.setTitle("Task 2");
		task2.setDescription("Description 2");
		task2.setDueDate("2024-05-22");
		task2.setCompleted(true);

		List<Task> mockTasks = Arrays.asList(task1, task2);

		when(taskRepository.findAll()).thenReturn(mockTasks);

		List<TaskDto> result = taskService.getAllTasks();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	public void testUpdateTask_Success() {
		long taskId = 1L;
		Task existingTask = new Task();
		existingTask.setId(taskId);
		existingTask.setTitle("Existing Task");
		existingTask.setDescription("Existing Description");
		existingTask.setDueDate("2024-05-21");
		existingTask.setCompleted(false);

		TaskDto updatedTaskDto = new TaskDto();
		updatedTaskDto.setTitle("Updated Task");
		updatedTaskDto.setDescription("Updated Description");
		updatedTaskDto.setDueDate("2024-06-21");
		updatedTaskDto.setCompleted(true);

		when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
		when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

		TaskDto result = taskService.updateTask(taskId, updatedTaskDto);

		assertNotNull(result);
		assertEquals(updatedTaskDto.getTitle(), result.getTitle());
		assertEquals(updatedTaskDto.getDescription(), result.getDescription());
		assertEquals(updatedTaskDto.getDueDate(), result.getDueDate());
		assertTrue(result.isCompleted());
	}

	@Test
	public void testDeleteTask_Success() {
		long taskId = 1L;
		Task task = new Task();
		task.setId(taskId);
		task.setTitle("Task to be deleted");
		task.setDescription("Description");
		task.setDueDate("2024-05-21");
		task.setCompleted(false);

		when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
		doNothing().when(taskRepository).deleteById(taskId);

		taskService.deleteTask(taskId);

		verify(taskRepository, times(1)).deleteById(taskId);
	}

	@Test
	public void testUpdateCompletedStatus_Success() {
		long taskId = 1L;
		Task task = new Task();
		task.setId(taskId);
		task.setTitle("Task to be updated");
		task.setDescription("Description");
		task.setDueDate("2024-05-21");
		task.setCompleted(false);

		when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
		when(taskRepository.save(any(Task.class))).thenReturn(task);

		TaskDto result = taskService.updateCompletedStatus(taskId, true);

		assertNotNull(result);
		assertTrue(result.isCompleted());
	}

	@Test
	public void testHandleNullOrEmptyInput() {
		long taskId = 1L;

		assertThrows(RuntimeException.class, () -> {
			taskService.updateTask(taskId, null);
		});

		assertThrows(RuntimeException.class, () -> {
			taskService.createTask(null);
		});

		TaskDto emptyTaskDto = new TaskDto();

		assertThrows(RuntimeException.class, () -> {
			taskService.createTask(emptyTaskDto);
		});
	}

	@Test
	public void testHandleInvalidTaskIds() {
		long invalidTaskId = -1L;

		assertThrows(RuntimeException.class, () -> {
			taskService.getTaskById(invalidTaskId);
		});

		assertThrows(RuntimeException.class, () -> {
			taskService.updateTask(invalidTaskId, new TaskDto());
		});

		assertThrows(RuntimeException.class, () -> {
			taskService.deleteTask(invalidTaskId);
		});

		assertThrows(RuntimeException.class, () -> {
			taskService.updateCompletedStatus(invalidTaskId, true);
		});
	}

	@Test
	public void testHandleDatabaseErrors() {
		long taskId = 1L;

		when(taskRepository.findById(taskId)).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> {
			taskService.getTaskById(taskId);
		});
	}

}
