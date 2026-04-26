package com.example.basictodo.di

import androidx.room.Room
import com.example.basictodo.data.local.db.TaskDatabase
import com.example.basictodo.data.repository.TaskRepositoryImpl
import com.example.basictodo.domain.repository.TaskRepository
import com.example.basictodo.domain.usecase.*
import com.example.basictodo.ui.addedit.AddEditTaskViewModel
import com.example.basictodo.ui.taskdetail.TaskDetailViewModel
import com.example.basictodo.ui.tasklist.TaskListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME
        ).build()
    }

    single { get<TaskDatabase>().taskDao }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { GetTasksUseCase(get()) }
    single { ToggleTaskCompletionUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { SaveTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
}

val viewModelModule = module {
    viewModel { TaskListViewModel(get(), get()) }
    viewModel { (taskId: Int?) -> AddEditTaskViewModel(taskId, get(), get()) }
    viewModel { (taskId: Int) -> TaskDetailViewModel(taskId, get(), get()) }
}

val appModule = listOf(databaseModule, repositoryModule, useCaseModule, viewModelModule)
