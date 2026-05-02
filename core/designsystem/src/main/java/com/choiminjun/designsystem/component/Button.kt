package com.choiminjun.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.CoolNeutral96
import com.choiminjun.designsystem.theme.SemanticFgOnPrimary
import com.choiminjun.designsystem.theme.SemanticFgPrimary
import com.choiminjun.designsystem.theme.SemanticPrimary
import com.choiminjun.designsystem.theme.SemanticStrokeDefault
import com.choiminjun.designsystem.theme.Shape

enum class SRButtonSize { Default, Small }

@Composable
fun SRSolidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: SRButtonSize = SRButtonSize.Default,
    enabled: Boolean = true,
) {
    val (height, style, padding) = when (size) {
        SRButtonSize.Default -> Triple(48.dp, MaterialTheme.typography.labelLarge, PaddingValues(horizontal = 28.dp, vertical = 12.dp))
        SRButtonSize.Small -> Triple(36.dp, MaterialTheme.typography.labelMedium, PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    }
    val shape = if (size == SRButtonSize.Default) Shape.md else Shape.sm

    Button(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = SemanticPrimary,
            contentColor = SemanticFgOnPrimary,
            disabledContainerColor = SemanticPrimary.copy(alpha = 0.36f),
            disabledContentColor = SemanticFgOnPrimary.copy(alpha = 0.36f),
        ),
        contentPadding = padding,
    ) {
        Text(text = text, style = style)
    }
}

@Composable
fun SROutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: SRButtonSize = SRButtonSize.Default,
    enabled: Boolean = true,
) {
    val (height, style, padding) = when (size) {
        SRButtonSize.Default -> Triple(48.dp, MaterialTheme.typography.labelLarge, PaddingValues(horizontal = 28.dp, vertical = 12.dp))
        SRButtonSize.Small -> Triple(36.dp, MaterialTheme.typography.labelMedium, PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    }
    val shape = if (size == SRButtonSize.Default) Shape.md else Shape.sm

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = SemanticFgPrimary,
            disabledContainerColor = CoolNeutral96,
            disabledContentColor = SemanticFgPrimary.copy(alpha = 0.36f),
        ),
        border = BorderStroke(1.dp, SemanticStrokeDefault),
        contentPadding = padding,
    ) {
        Text(text = text, style = style)
    }
}

@Composable
fun SRTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: SRButtonSize = SRButtonSize.Default,
    enabled: Boolean = true,
) {
    val (height, style, padding) = when (size) {
        SRButtonSize.Default -> Triple(48.dp, MaterialTheme.typography.labelLarge, PaddingValues(horizontal = 12.dp, vertical = 12.dp))
        SRButtonSize.Small -> Triple(36.dp, MaterialTheme.typography.labelMedium, PaddingValues(horizontal = 12.dp, vertical = 8.dp))
    }

    TextButton(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = Shape.md,
        colors = ButtonDefaults.textButtonColors(
            contentColor = SemanticPrimary,
            disabledContentColor = SemanticPrimary.copy(alpha = 0.36f),
        ),
        contentPadding = padding,
    ) {
        Text(text = text, style = style)
    }
}
