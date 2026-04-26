package com.example.basictodo.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basictodo.domain.usecase.DeleteTaskUseCase
import com.example.basictodo.domain.usecase.GetTaskByIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val taskId: Int,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    private val _effect = Channel<TaskDetailEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadTask()
    }

    fun handleIntent(intent: TaskDetailIntent) {
        when (intent) {
            is TaskDetailIntent.DeleteTask -> deleteTask()
            is TaskDetailIntent.OnEditClick -> {
                viewModelScope.launch { _effect.send(TaskDetailEffect.NavigateToEdit(taskId)) }
            }
        }
    }

    private fun loadTask() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val task = getTaskByIdUseCase(taskId)
                if (task != null) {
                    _state.update { it.copy(task = task, isLoading = false) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Task not found") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun deleteTask() {
        val task = _state.value.task ?: return
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task)
                _effect.send(TaskDetailEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.send(TaskDetailEffect.ShowSnackbar("Failed to delete task"))
            }
        }
    }
}
