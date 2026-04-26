package com.example.basictodo.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object TaskList : Screen

    @Serializable
    data class TaskDetail(val taskId: Int) : Screen

    @Serializable
    data class AddEditTask(val taskId: Int? = null) : Screen
}
