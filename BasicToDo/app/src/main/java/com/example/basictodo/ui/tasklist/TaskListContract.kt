package com.example.basictodo.ui.tasklist

import com.example.basictodo.domain.model.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface TaskListIntent {
    data object LoadTasks : TaskListIntent
    data class ToggleTask(val task: Task) : TaskListIntent
    data class DeleteTask(val task: Task) : TaskListIntent
    data class OnTaskClick(val taskId: Int) : TaskListIntent
    data object OnAddTaskClick : TaskListIntent
}

sealed interface TaskListEffect {
    data class NavigateToDetail(val taskId: Int) : TaskListEffect
    data object NavigateToAddEdit : TaskListEffect
    data class ShowSnackbar(val message: String) : TaskListEffect
}
