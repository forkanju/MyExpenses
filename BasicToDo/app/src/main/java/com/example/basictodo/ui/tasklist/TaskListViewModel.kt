package com.example.basictodo.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basictodo.domain.usecase.GetTasksUseCase
import com.example.basictodo.domain.usecase.ToggleTaskCompletionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state.asStateFlow()

    private val _effect = Channel<TaskListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(TaskListIntent.LoadTasks)
    }

    fun handleIntent(intent: TaskListIntent) {
        when (intent) {
            is TaskListIntent.LoadTasks -> loadTasks()
            is TaskListIntent.ToggleTask -> toggleTask(intent)
            is TaskListIntent.DeleteTask -> {} // To be implemented
            is TaskListIntent.OnTaskClick -> {
                viewModelScope.launch { _effect.send(TaskListEffect.NavigateToDetail(intent.taskId)) }
            }
            is TaskListIntent.OnAddTaskClick -> {
                viewModelScope.launch { _effect.send(TaskListEffect.NavigateToAddEdit) }
            }
        }
    }

    private fun loadTasks() {
        getTasksUseCase()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { tasks ->
                _state.update { it.copy(tasks = tasks, isLoading = false) }
            }
            .catch { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun toggleTask(intent: TaskListIntent.ToggleTask) {
        viewModelScope.launch {
            try {
                toggleTaskCompletionUseCase(intent.task)
            } catch (e: Exception) {
                _effect.send(TaskListEffect.ShowSnackbar("Failed to update task"))
            }
        }
    }
}
