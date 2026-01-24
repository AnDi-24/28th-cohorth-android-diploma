package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.presentation.BottomNavigationBar
import ru.practicum.android.diploma.presentation.FavoriteScreen
import ru.practicum.android.diploma.presentation.MainScreen
import ru.practicum.android.diploma.presentation.TeamScreen

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "main",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("main") { MainScreen() }
                    composable("favorite") { FavoriteScreen() }
                    composable("team") { TeamScreen() }
                }
            }
        }
    }
}
