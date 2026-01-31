package ru.practicum.android.diploma.ui.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.AppTheme

@Composable
private fun BaseAppButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    isEnabled: Boolean = true,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(59.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 20.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PositiveButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    BaseAppButton(
        text = text,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        isEnabled = isEnabled,
        modifier = modifier
    )
}

@Composable
fun NegativeButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    BaseAppButton(
        text = text,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.tertiary,
        isEnabled = isEnabled,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PositiveButtonPreview() {
    AppTheme {
        PositiveButton(
            text = R.string.reset,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NegativeButtonPreview() {
    AppTheme {
        NegativeButton(
            text = R.string.reset,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PositiveAndNegativeButtonsPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            PositiveButton(
                text = R.string.select,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            NegativeButton(
                text = R.string.reset,
                onClick = {}
            )
        }
    }
}
