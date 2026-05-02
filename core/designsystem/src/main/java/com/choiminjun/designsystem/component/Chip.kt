package com.choiminjun.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.Blue95
import com.choiminjun.designsystem.theme.CoolNeutral10
import com.choiminjun.designsystem.theme.Green50
import com.choiminjun.designsystem.theme.Green95
import com.choiminjun.designsystem.theme.Red50
import com.choiminjun.designsystem.theme.Red95
import com.choiminjun.designsystem.theme.SemanticFgOnPrimary
import com.choiminjun.designsystem.theme.SemanticPrimary
import com.choiminjun.designsystem.theme.SemanticStrokeDefault
import com.choiminjun.designsystem.theme.Shape
import com.choiminjun.designsystem.theme.Yellow46
import com.choiminjun.designsystem.theme.Yellow95

@Composable
fun SRFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = label, style = MaterialTheme.typography.labelMedium) },
        modifier = modifier,
        shape = Shape.pill,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = CoolNeutral10,
            selectedContainerColor = SemanticPrimary,
            selectedLabelColor = SemanticFgOnPrimary,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = SemanticStrokeDefault,
            selectedBorderColor = SemanticPrimary,
            borderWidth = 1.dp,
            selectedBorderWidth = 0.dp,
            enabled = true,
            selected = selected,
        ),
    )
}

enum class SRBadgeStyle { New, Info, Positive, Cautionary, Negative }

@Composable
fun SRBadge(
    text: String,
    style: SRBadgeStyle = SRBadgeStyle.Info,
) {
    val (containerColor, contentColor) = when (style) {
        SRBadgeStyle.New -> SemanticPrimary to SemanticFgOnPrimary
        SRBadgeStyle.Info -> Blue95 to SemanticPrimary
        SRBadgeStyle.Positive -> Green95 to Green50
        SRBadgeStyle.Cautionary -> Yellow95 to Yellow46
        SRBadgeStyle.Negative -> Red95 to Red50
    }

    Surface(
        shape = Shape.xs,
        color = containerColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}
