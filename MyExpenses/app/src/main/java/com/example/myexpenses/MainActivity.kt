package com.example.myexpenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.myexpenses.navigation.Analysis
import com.example.myexpenses.navigation.Dashboard
import com.example.myexpenses.navigation.Navigator
import com.example.myexpenses.navigation.rememberNavigationState
import com.example.myexpenses.navigation.toEntries
import com.example.myexpenses.ui.AnalysisScreen
import com.example.myexpenses.ui.DashboardScreen
import com.example.myexpenses.ui.theme.MyExpensesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyExpensesTheme {
                val navigationState = rememberNavigationState(
                    startRoute = Dashboard,
                    topLevelRoutes = setOf(Dashboard, Analysis)
                )
                val navigator = remember { Navigator(navigationState) }

                val viewModel: MainViewModel = viewModel()

                val entryProvider = entryProvider<NavKey> {
                    entry<Dashboard> {
                        DashboardScreen(viewModel = viewModel)
                    }
                    entry<Analysis> {
                        AnalysisScreen(viewModel = viewModel)
                    }
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                                label = { Text("Transactions") },
                                selected = navigationState.topLevelRoute == Dashboard,
                                onClick = { navigator.navigate(Dashboard) }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Analytics, contentDescription = null) },
                                label = { Text("Analysis") },
                                selected = navigationState.topLevelRoute == Analysis,
                                onClick = { navigator.navigate(Analysis) }
                            )
                        }
                    },
                    floatingActionButton = {
                        if (navigationState.topLevelRoute == Dashboard) {
                            FloatingActionButton(
                                onClick = { viewModel.openAddExpense() },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Expense")
                            }
                        }
                    },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        entries = navigationState.toEntries(entryProvider),
                        onBack = { navigator.goBack() }
                    )
                }
            }
        }
    }
}
