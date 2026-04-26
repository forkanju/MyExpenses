package com.example.basictodo.domain.usecase

import com.example.basictodo.domain.model.Task
import com.example.basictodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.getAllTasks()
}
