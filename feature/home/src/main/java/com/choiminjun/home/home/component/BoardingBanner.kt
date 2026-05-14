package com.choiminjun.home.home.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.home.R
import com.choiminjun.designsystem.R as DesignR

@Composable
internal fun BoardingBanner(
    routeNo: String,
    destNodeName: String,
    stopsBeforeAlarm: Int,
    onClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(12.dp), color = SRTheme.colors.blue60)
            .clickable { onClick() }
            .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF34C759), CircleShape),
            )
            Spacer(Modifier.width(Spacing.space6))
            Text(
                text = stringResource(R.string.alarm_banner_status),
                style = SRTheme.typography.bodyXSSB,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SRTheme.colors.blue70)
                    .padding(vertical = Spacing.space8, horizontal = Spacing.space12)
                    .clickable { onCancelClick() },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(DesignR.drawable.ic_close),
                        contentDescription = null,
                        tint = Color.White,
                    )
                    Text(
                        text = stringResource(R.string.alarm_cancel_button),
                        style = SRTheme.typography.bodyXSSB,
                        color = Color.White,
                    )
                }
            }
        }
        Spacer(Modifier.height(Spacing.space16))
        Text(
            text = stringResource(R.string.alarm_banner_route_no, routeNo),
            style = SRTheme.typography.bodyXMM,
            color = Color.White,
        )
        Spacer(Modifier.height(Spacing.space8))
        Text(
            text = stringResource(R.string.alarm_banner_dest, destNodeName, stopsBeforeAlarm),
            style = SRTheme.typography.bodySR,
            color = Color.White,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun BoardingBannerPreview() {
    SRTheme {
        BoardingBanner(
            routeNo = "51",
            destNodeName = "하단",
            stopsBeforeAlarm = 3,
            onClick = {},
            onCancelClick = {},
        )
    }
}
