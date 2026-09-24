package com.easycare.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.easycare.app.model.TextSize

private val EasyCareColors = lightColorScheme(
    primary = EasyCareGreen,
    onPrimary = Color.White,
    primaryContainer = EasyCareGreenContainer,
    onPrimaryContainer = EasyCareInk,
    error = EasyCareRed,
    onError = Color.White,
    errorContainer = EasyCareRedContainer,
    onErrorContainer = EasyCareRedDark,
    background = Color.White,
    onBackground = EasyCareInk,
    surface = Color.White,
    onSurface = EasyCareInk,
    surfaceVariant = EasyCareSurface,
    onSurfaceVariant = EasyCareMuted,
    outline = EasyCareOutline,
)

data class EasyCareSpacing(
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
)

val AppSpacing = EasyCareSpacing()

@Composable
fun EasyCareTheme(textSize: TextSize = TextSize.Normal, content: @Composable () -> Unit) {
    val scale = textSize.multiplier
    MaterialTheme(
        colorScheme = EasyCareColors,
        typography = EasyCareTypography.copy(
            bodyLarge = EasyCareTypography.bodyLarge.copy(fontSize = EasyCareTypography.bodyLarge.fontSize * scale),
            bodyMedium = EasyCareTypography.bodyMedium.copy(fontSize = EasyCareTypography.bodyMedium.fontSize * scale),
            labelLarge = EasyCareTypography.labelLarge.copy(fontSize = EasyCareTypography.labelLarge.fontSize * scale),
            titleMedium = EasyCareTypography.titleMedium.copy(fontSize = EasyCareTypography.titleMedium.fontSize * scale),
        ),
        content = content,
    )
}
