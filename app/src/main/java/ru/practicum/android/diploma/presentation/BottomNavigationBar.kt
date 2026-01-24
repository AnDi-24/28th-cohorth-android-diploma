package ru.practicum.android.diploma.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.models.BottomNavigationItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            "main",
            stringResource(R.string.main_screen),
            R.drawable.search),
        BottomNavigationItem(
            "favorite",
            stringResource(R.string.favorite_screen),
            R.drawable.favorite),
        BottomNavigationItem(
            "team",
            stringResource(R.string.team_screen),
            R.drawable.team)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val selectedColor = colorResource(R.color.blue)
    val unselectedColor = colorResource(R.color.gray)

    BottomNavigation(
        backgroundColor = Color.White,
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Image(
                        modifier = Modifier.padding(bottom = 2.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        colorFilter = ColorFilter.tint(
                            if (currentRoute == item.route) selectedColor else unselectedColor
                        )
                    )
                },
                label = { Text(
                    item.label,
                    fontSize = 12.sp,
                    color = if (currentRoute == item.route) selectedColor else unselectedColor) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


