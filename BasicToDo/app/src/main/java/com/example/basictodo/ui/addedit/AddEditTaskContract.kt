package com.example.basictodo.ui.addedit

data class AddEditTaskState(
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val error: String? = null
)

sealed interface AddEditTaskIntent {
    data class OnTitleChange(val title: String) : AddEditTaskIntent
    data class OnDescriptionChange(val description: String) : AddEditTaskIntent
    data object SaveTask : AddEditTaskIntent
}

sealed interface AddEditTaskEffect {
    data object NavigateBack : AddEditTaskEffect
    data class ShowSnackbar(val message: String) : AddEditTaskEffect
}
