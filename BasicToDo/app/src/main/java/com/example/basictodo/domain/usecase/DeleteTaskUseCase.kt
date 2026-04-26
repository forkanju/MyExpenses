package com.example.basictodo.domain.usecase

import com.example.basictodo.domain.model.Task
import com.example.basictodo.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.deleteTask(task)
}
