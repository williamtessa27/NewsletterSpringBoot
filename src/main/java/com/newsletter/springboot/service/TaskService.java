package com.newsletter.springboot.service;

import com.newsletter.springboot.dto.CreateTaskDTO;
import com.newsletter.springboot.dto.TaskResponseDTO;
import com.newsletter.springboot.dto.UpdateTaskDTO;
import com.newsletter.springboot.exception.TaskNotFoundException;
import com.newsletter.springboot.mapper.TaskMapper;
import com.newsletter.springboot.model.Task;
import com.newsletter.springboot.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO createTask(CreateTaskDTO dto) {
        Task task = TaskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toResponseDTO(task);
    }

    public List<TaskResponseDTO> getCompletedTasks() {
        return taskRepository.findByCompleted(true)
                .stream()
                .map(TaskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO updateTask(Long id, UpdateTaskDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        TaskMapper.updateEntity(task, dto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponseDTO(savedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
