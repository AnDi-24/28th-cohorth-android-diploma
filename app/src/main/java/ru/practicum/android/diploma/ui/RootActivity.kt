package ru.practicum.android.diploma.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.practicum.android.diploma.ui.compose.NavGraph
import ru.practicum.android.diploma.ui.theme.AppTheme

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }
}
