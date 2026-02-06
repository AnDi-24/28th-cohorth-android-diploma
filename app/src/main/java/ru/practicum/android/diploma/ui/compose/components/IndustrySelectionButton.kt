package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.ButtonHeight
import ru.practicum.android.diploma.ui.theme.RadiusMedium
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing4
import ru.practicum.android.diploma.ui.theme.Spacing8
import ru.practicum.android.diploma.ui.theme.fontLineHeight19
import ru.practicum.android.diploma.ui.theme.fontSize16

@Composable
fun IndustrySelectionButton(
    modifier: Modifier = Modifier,
    isIndustrySelected: Boolean,
    selectedIndustryName: String?,
    onClearClick: () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight),
        shape = RoundedCornerShape(RadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        if (isIndustrySelected && selectedIndustryName != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(ButtonHeight)
                        .clickable { onClick() }
                        .padding(
                            top = 6.dp,
                            bottom = 6.dp,
                            start = Spacing16,
                            end = Spacing8
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.industry),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(Spacing4))
                        Text(
                            text = selectedIndustryName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = fontSize16,
                                lineHeight = fontLineHeight19,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onClearClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cross_icon),
                        contentDescription = stringResource(id = R.string.clear_industry_filter),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Текст с собственными паддингами
                Text(
                    text = stringResource(id = R.string.industry),
                    modifier = Modifier
                        .weight(1f)
                        .height(ButtonHeight)
                        .clickable { onClick() }
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp,
                            start = Spacing16,
                            end = Spacing8
                        ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Start
                )

                // Иконка стрелочки - отдельный кликабельный элемент
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.leading_icon),
                        contentDescription = stringResource(id = R.string.filter_option)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun IndustrySelectionButtonPreview_Multiple() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(Spacing16)
        ) {
            // Пример 1: Отрасль не выбрана
            IndustrySelectionButton(
                modifier = Modifier.fillMaxWidth(),
                isIndustrySelected = false,
                selectedIndustryName = null,
                onClearClick = {},
                onClick = {}
            )

            // Пример 2: Среднее название
            IndustrySelectionButton(
                modifier = Modifier.fillMaxWidth(),
                isIndustrySelected = true,
                selectedIndustryName = "Маркетинг",
                onClearClick = {},
                onClick = {}
            )

            // Пример 3: Очень длинное название
            IndustrySelectionButton(
                modifier = Modifier.fillMaxWidth(),
                isIndustrySelected = true,
                selectedIndustryName = "Информационные технологии, интернет, телекоммуникации",
                onClearClick = {},
                onClick = {}
            )
        }
    }
}
