package com.example.basictodo.domain.usecase

import com.example.basictodo.domain.model.Task
import com.example.basictodo.domain.repository.TaskRepository

class SaveTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        if (task.id == 0) {
            repository.insertTask(task)
        } else {
            repository.updateTask(task)
        }
    }
}
