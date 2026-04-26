package com.example.basictodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.basictodo.ui.addedit.AddEditTaskScreen
import com.example.basictodo.ui.navigation.Screen
import com.example.basictodo.ui.taskdetail.TaskDetailScreen
import com.example.basictodo.ui.tasklist.TaskListScreen
import com.example.basictodo.ui.theme.BasicToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicToDoTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TaskList
    ) {
        composable<Screen.TaskList> {
            TaskListScreen(
                onNavigateToDetail = { taskId ->
                    navController.navigate(Screen.TaskDetail(taskId))
                },
                onNavigateToAddEdit = {
                    navController.navigate(Screen.AddEditTask())
                }
            )
        }

        composable<Screen.TaskDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TaskDetail>()
            TaskDetailScreen(
                taskId = args.taskId,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { taskId ->
                    navController.navigate(Screen.AddEditTask(taskId))
                }
            )
        }

        composable<Screen.AddEditTask> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AddEditTask>()
            AddEditTaskScreen(
                taskId = args.taskId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
