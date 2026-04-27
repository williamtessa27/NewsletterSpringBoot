package com.newsletter.springboot.mapper;

import com.newsletter.springboot.dto.CreateTaskDTO;
import com.newsletter.springboot.dto.TaskResponseDTO;
import com.newsletter.springboot.dto.UpdateTaskDTO;
import com.newsletter.springboot.model.Task;

public class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(CreateTaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(false);
        return task;
    }

    public static void updateEntity(Task task, UpdateTaskDTO dto) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
    }

    public static TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        return dto;
    }
}
