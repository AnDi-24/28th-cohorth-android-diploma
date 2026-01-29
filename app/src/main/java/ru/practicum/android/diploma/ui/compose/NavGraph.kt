package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.models.NavItem
import ru.practicum.android.diploma.ui.compose.components.TopBar
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.Typography

const val MAIN = "main"
const val FAVORITE = "favorite"
const val TEAM = "team"
const val FILTER = "filter"
const val OPTION = "option"
const val VACANCY = "vacancy"

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = when (currentRoute) {
        MAIN, FAVORITE, TEAM -> true
        else -> false
    }

    val topBar: @Composable () -> Unit = {
        when (currentRoute) {
            MAIN -> {
                TopBar(
                    title = stringResource(R.string.main_screen),
                    null,
                    {
                        IconButton(onClick = {
                            navController.navigate(FILTER)
                        }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filters),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
            TEAM -> {
                TopBar(
                    title = stringResource(R.string.team_screen),
                )
            }
            FILTER -> {
                TopBar(
                    title = stringResource(R.string.filter),
                    { navController.popBackStack() }
                )
            }
            VACANCY -> {
                TopBar(
                    title = stringResource(R.string.vacancy),
                    { navController.popBackStack() },
                    {
                        IconButton(onClick = { /* логика поделиться */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sharing),
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /* логика избранное */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorites_off),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
            OPTION -> {
                TopBar(
                    title = stringResource(R.string.filter_option),
                    { navController.popBackStack() }
                )
            }
            else ->
                TopBar(
                    title = stringResource(R.string.favorite_screen)
                )
        }
    }
    Scaffold(
        topBar = topBar,
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MAIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MAIN) {
                MainScreen(navController)
            }
            composable(FAVORITE) {
                FavoriteScreen(navController)
            }
            composable(TEAM) {
                TeamScreen()
            }
            composable(FILTER) {
                FilterScreen(navController)
            }
            composable(VACANCY) {
                VacancyScreen(navController)
            }
            composable(OPTION) {
                FilterOptionScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val colors = MaterialTheme.colorScheme

    val items = listOf(
        NavItem(
            MAIN,
            stringResource(R.string.main_screen),
            R.drawable.search
        ),
        NavItem(
            FAVORITE,
            stringResource(R.string.favorite_screen),
            R.drawable.favorite
        ),
        NavItem(
            TEAM,
            stringResource(R.string.team_screen),
            R.drawable.team
        )
    )

    val selectedColor = colors.primary
    val unselectedColor = colors.onSurfaceVariant

    NavigationBar(
        containerColor = colors.background,
        modifier = Modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    color = LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Image(
                        modifier = Modifier.padding(bottom = 2.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        colorFilter = ColorFilter.tint(
                            if (isSelected) selectedColor else unselectedColor
                        )
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected) selectedColor else unselectedColor,
                        style = Typography.labelSmall
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    unselectedIconColor = colors.onSurfaceVariant,
                    unselectedTextColor = colors.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
