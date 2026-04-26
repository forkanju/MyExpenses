package com.example.basictodo.domain.usecase

import com.example.basictodo.domain.model.Task
import com.example.basictodo.domain.repository.TaskRepository

class GetTaskByIdUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int): Task? = repository.getTaskById(id)
}
