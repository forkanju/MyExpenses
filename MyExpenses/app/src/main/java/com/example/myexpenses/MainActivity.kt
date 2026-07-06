package com.example.myexpenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.myexpenses.navigation.Dashboard
import com.example.myexpenses.navigation.Navigator
import com.example.myexpenses.navigation.rememberNavigationState
import com.example.myexpenses.navigation.toEntries
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
                    topLevelRoutes = setOf(Dashboard)
                )
                val navigator = remember { Navigator(navigationState) }

                val viewModel: MainViewModel = viewModel()

                val entryProvider = entryProvider<NavKey> {
                    entry<Dashboard> {
                        DashboardScreen(viewModel = viewModel)
                    }
                }

                NavDisplay(
                    entries = navigationState.toEntries(entryProvider),
                    onBack = { navigator.goBack() }
                )
            }
        }
    }
}
