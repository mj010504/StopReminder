package com.choiminjun.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme

@Composable
fun SRSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData,
) {
    val message = snackbarData.visuals.message

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SRTheme.colors.coolNeutral30)
            .padding(vertical = 14.dp, horizontal = 20.dp),
    ) {
        Text(
            text = message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = SRTheme.typography.bodyMSB,
            color = SRTheme.colors.white,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SRSnackbarPreview() {
    SRTheme {
        SRSnackbar(
            snackbarData = object : SnackbarData {
                override val visuals: SnackbarVisuals = object : SnackbarVisuals {
                    override val actionLabel = null
                    override val duration = SnackbarDuration.Short
                    override val message = "즐겨찾기에 추가되었습니다."
                    override val withDismissAction = false
                }

                override fun dismiss() { /* No-op */ }
                override fun performAction() { /* No-op */ }
            },
        )
    }
}
