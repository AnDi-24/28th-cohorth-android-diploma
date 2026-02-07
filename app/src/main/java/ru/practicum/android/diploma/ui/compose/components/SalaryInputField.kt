package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Gray
import ru.practicum.android.diploma.ui.theme.LightGray
import ru.practicum.android.diploma.ui.theme.RadiusMedium
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing4
import ru.practicum.android.diploma.ui.theme.Spacing8
import ru.practicum.android.diploma.ui.theme.fontLineHeight19
import ru.practicum.android.diploma.ui.theme.fontSize16

@Composable
fun SalaryInputField(
    modifier: Modifier = Modifier,
    salaryText: String,
    onSalaryChanged: (String) -> Unit,
    onClearClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(51.dp)
            .clip(RoundedCornerShape(RadiusMedium))
            .background(LightGray)
            .padding(
                start = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
                .padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 40.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.expected_salary),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isFocused) Blue else Gray
                    ),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(Spacing4))

                BasicTextField(
                    value = salaryText,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        onSalaryChanged(filtered)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    textStyle = LocalTextStyle.current.merge(
                        TextStyle(
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            fontSize = fontSize16,
                            lineHeight = fontLineHeight19,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ),
                    cursorBrush = SolidColor(Blue),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (salaryText.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.enter_salary),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = fontSize16,
                                        lineHeight = fontLineHeight19,
                                        color = Gray
                                    ),
                                    textAlign = TextAlign.Start
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        if (salaryText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { onClearClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cross_icon),
                    contentDescription = stringResource(id = R.string.clear_salary_field)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SalaryInputFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing16)
        ) {
            Text(
                text = "Пустое поле (не в фокусе)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            SalaryInputField(
                salaryText = "",
                onSalaryChanged = {},
                onClearClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "С введенным значением - виден крестик",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = Spacing8)
            )
            var salaryText3 by remember { mutableStateOf("100000000000000000000000000000000") }
            SalaryInputField(
                salaryText = salaryText3,
                onSalaryChanged = { salaryText3 = it },
                onClearClick = { salaryText3 = "" },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
