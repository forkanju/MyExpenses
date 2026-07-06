package com.example.dailygrocery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.dailygrocery.navigation.Route
import com.example.dailygrocery.ui.home.HomeScreen
import com.example.dailygrocery.ui.onboarding.OnboardingScreen
import com.example.dailygrocery.ui.theme.DailyGroceryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyGroceryTheme {
                val backStack = rememberNavBackStack(Route.Onboarding)
                
                NavDisplay(
                    backStack = backStack,
                    entryProvider = { key ->
                        when (key) {
                            Route.Onboarding -> NavEntry(key) {
                                OnboardingScreen(
                                    onFinished = {
                                        backStack.add(Route.Home)
                                    }
                                )
                            }
                            Route.Home -> NavEntry(key) {
                                HomeScreen(
                                    onBack = {
                                        if (backStack.size > 1) {
                                            backStack.removeAt(backStack.size - 1)
                                        }
                                    }
                                )
                            }
                            else -> NavEntry(key) {
                                Text("Unknown route: $key")
                            }
                        }
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator()
                    ),
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        } else {
                            finish()
                        }
                    }
                )
            }
        }
    }
}
