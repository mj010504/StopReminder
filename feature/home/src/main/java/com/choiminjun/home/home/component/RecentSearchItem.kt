package com.choiminjun.home.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch
import com.choiminjun.designsystem.R as DR

@Composable
internal fun RecentRouteSearchItem(
    item: RecentRouteSearch,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.space8)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.space8)) {
                    BusTypeLabel(type = item.routeType)
                    Text(text = item.routeNo, style = SRTheme.typography.bodyMM, color = SRTheme.colors.textPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.space4)) {
                    Text(item.startNodeName, style = SRTheme.typography.bodyMM, color = SRTheme.colors.textPrimary)
                    Icon(
                        imageVector = ImageVector.vectorResource(DR.drawable.ic_horizontal_arrow),
                        contentDescription = null,
                        modifier = Modifier.size(Spacing.space16),
                        tint = SRTheme.colors.textPrimary,
                    )
                    Text(item.endNodeName, style = SRTheme.typography.bodyMM, color = SRTheme.colors.textPrimary)
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(DR.drawable.ic_close),
                    contentDescription = "삭제",
                    tint = SRTheme.colors.icon,
                    modifier = Modifier.size(Spacing.space28),
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
    }
}

@Composable
internal fun RecentNodeSearchItem(
    item: RecentNodeSearch,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.space8)) {
                Text(text = item.nodeName, style = SRTheme.typography.bodyMM, color = SRTheme.colors.textPrimary)
                item.nodeNo?.let {
                    Text(text = it, style = SRTheme.typography.bodySR, color = SRTheme.colors.textSecondary)
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(DR.drawable.ic_close),
                    contentDescription = "삭제",
                    tint = SRTheme.colors.icon,
                    modifier = Modifier.size(Spacing.space28),
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentRouteSearchItemPreview() {
    SRTheme {
        RecentRouteSearchItem(
            item = RecentRouteSearch(
                id = 1,
                routeNo = "1001",
                routeType = "직행버스",
                startNodeName = "기장",
                endNodeName = "하단",
                cityCode = CityCode.BUSAN,
                routeId = "",
            ),
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentNodeSearchItemPreview() {
    SRTheme {
        RecentNodeSearchItem(
            item = RecentNodeSearch(
                id = 1,
                nodeId = "BSE123",
                nodeName = "서면역.서면지하상가",
                nodeNo = "05231",
                cityCode = CityCode.BUSAN,
            ),
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}
