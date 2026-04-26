package com.example.basictodo.ui.taskdetail

import com.example.basictodo.domain.model.Task

data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface TaskDetailIntent {
    data object DeleteTask : TaskDetailIntent
    data object OnEditClick : TaskDetailIntent
}

sealed interface TaskDetailEffect {
    data object NavigateBack : TaskDetailEffect
    data class NavigateToEdit(val taskId: Int) : TaskDetailEffect
    data class ShowSnackbar(val message: String) : TaskDetailEffect
}
