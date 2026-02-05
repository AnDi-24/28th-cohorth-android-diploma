package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.FilterViewModel
import ru.practicum.android.diploma.ui.theme.Spacing16

private const val MOK_SALARY = 14000

@Composable
fun FilterScreen(navController: NavController) {
    val viewModel: FilterViewModel = koinViewModel()
    val filterData by viewModel.filterState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(OPTION) }) {
            Text("Выбрать опцию")
        }
        Button(
            onClick = {
                viewModel.loadFilters()
            }
        ) {
            Text(
                "Обновить отрасль"
            )
        }

        Button(
            onClick = {
                viewModel.updateSalary(MOK_SALARY)
            }
        ) {
            Text(
                "Обновить зарплату"
            )
        }

        Button(
            onClick = {
                viewModel.updateShowSalary(true)
            }
        ) {
            Text(
                "Обновить чек-бокс"
            )
        }
        Text(filterData.industry)
        Text(filterData.salary.toString())
        Text(filterData.showSalary.toString())
    }
}
