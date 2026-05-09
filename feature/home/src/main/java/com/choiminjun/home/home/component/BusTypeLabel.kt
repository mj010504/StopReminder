package com.choiminjun.home.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Shape
import com.choiminjun.designsystem.theme.Spacing

@Composable
internal fun BusTypeLabel(type: String) {
    Text(
        text = type,
        style = SRTheme.typography.bodyXSM,
        color = SRTheme.colors.white,
        modifier = Modifier
            .background(
                color = SRTheme.colors.blue50,
                shape = Shape.xs,
            )
            .padding(horizontal = Spacing.space6, vertical = Spacing.space2),
    )
}
