package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.RadiusMedium
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.fontLineHeight19
import ru.practicum.android.diploma.ui.theme.fontSize16

@Composable
fun HideSalaryFilterCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(RadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onCheckedChange(!isChecked) }
                .padding(
                    top = 6.dp,
                    end = 4.dp,
                    bottom = 6.dp,
                    start = Spacing16
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.hide_without_salary),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = fontSize16,
                    lineHeight = fontLineHeight19,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
                modifier = Modifier.size(40.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    disabledCheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledUncheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledIndeterminateColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HideSalaryFilterCheckboxPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(Spacing16),
            verticalArrangement = Arrangement.spacedBy(Spacing16)
        ) {
            HideSalaryFilterCheckbox(
                modifier = Modifier.fillMaxWidth(),
                isChecked = false,
                onCheckedChange = {}
            )

            HideSalaryFilterCheckbox(
                modifier = Modifier.fillMaxWidth(),
                isChecked = true,
                onCheckedChange = {}
            )
        }
    }
}
