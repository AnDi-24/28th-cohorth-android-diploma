package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.EmployerLogoGlide

@Composable
fun VacancyListItem(
    vacancy: VacancyDetailsModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            verticalAlignment = Alignment.Top
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                EmployerLogoGlide(
                    logoUrl = vacancy.employer?.logo,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                val formattedVacancyName = formatVacancyName(
                    vacancyName = vacancy.name,
                    companyName = vacancy.employer?.name
                )

                Text(
                    text = formattedVacancyName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = vacancy.employer?.name ?: "Название не указано",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                vacancy.salary?.let { salary ->
                    Text(
                        text = formatSalary(salary),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatVacancyName(vacancyName: String, companyName: String?): String {
    if (companyName.isNullOrBlank()) return vacancyName

    var result = vacancyName
    val lowerVacancyName = vacancyName.lowercase()
    val lowerCompanyName = companyName.lowercase()

    when {
        lowerVacancyName.endsWith(" в $lowerCompanyName") -> {
            result = vacancyName.removeSuffix(" в $companyName").trim()
        }

        lowerVacancyName.endsWith(lowerCompanyName) -> {
            result = vacancyName.removeSuffix(companyName).trim()
        }
    }

    return result
}

private fun formatSalary(salary: Salary?): String {
    return buildString {
        salary?.from?.let { from ->
            salary.to?.let { to ->
                append("от $from до $to")
            } ?: append("от $from")
        } ?: salary?.to?.let { to ->
            append("до $to")
        } ?: return "Зарплата не указана"

        salary?.currency?.let { currency ->
            append(" ${getCurrencySymbol(currency)}")
        }
    }
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency) {
        "RUR", "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }
}
