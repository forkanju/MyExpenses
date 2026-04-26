package com.example.basictodo.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basictodo.domain.model.Task
import com.example.basictodo.domain.usecase.GetTaskByIdUseCase
import com.example.basictodo.domain.usecase.SaveTaskUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddEditTaskViewModel(
    private val taskId: Int?,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditTaskState(isEditing = taskId != null))
    val state: StateFlow<AddEditTaskState> = _state.asStateFlow()

    private val _effect = Channel<AddEditTaskEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentTask: Task? = null

    init {
        taskId?.let { id ->
            loadTask(id)
        }
    }

    fun handleIntent(intent: AddEditTaskIntent) {
        when (intent) {
            is AddEditTaskIntent.OnTitleChange -> {
                _state.update { it.copy(title = intent.title) }
            }
            is AddEditTaskIntent.OnDescriptionChange -> {
                _state.update { it.copy(description = intent.description) }
            }
            is AddEditTaskIntent.SaveTask -> saveTask()
        }
    }

    private fun loadTask(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val task = getTaskByIdUseCase(id)
                if (task != null) {
                    currentTask = task
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Task not found") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun saveTask() {
        val title = _state.value.title
        val description = _state.value.description

        if (title.isBlank()) {
            viewModelScope.launch {
                _effect.send(AddEditTaskEffect.ShowSnackbar("Title cannot be empty"))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val taskToSave = currentTask?.copy(
                    title = title,
                    description = description
                ) ?: Task(
                    title = title,
                    description = description
                )

                saveTaskUseCase(taskToSave)
                _effect.send(AddEditTaskEffect.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.send(AddEditTaskEffect.ShowSnackbar("Failed to save task"))
            }
        }
    }
}
