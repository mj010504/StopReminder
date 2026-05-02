package com.choiminjun.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object Shape {
    val xs = RoundedCornerShape(4.dp)
    val sm = RoundedCornerShape(8.dp)
    val md = RoundedCornerShape(12.dp)
    val lg = RoundedCornerShape(16.dp)
    val xl = RoundedCornerShape(24.dp)
    val xxl = RoundedCornerShape(32.dp)
    val pill = RoundedCornerShape(percent = 50)
}

internal val SRShapes = Shapes(
    extraSmall = Shape.xs,
    small = Shape.sm,
    medium = Shape.md,
    large = Shape.lg,
    extraLarge = Shape.xl,
)
