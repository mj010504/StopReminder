package com.choiminjun.home.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.home.R
import com.choiminjun.designsystem.R as DR

@Composable
internal fun FavoriteSection(
    favoriteRoutes: List<BusRoute>,
    favoriteNodes: List<BusNode>,
    onRouteClick: (BusRoute) -> Unit,
    onNodeClick: (BusNode) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(SRTheme.colors.background),
    ) {
        if (favoriteRoutes.isNotEmpty() || favoriteNodes.isNotEmpty()) {
            item { SectionHeader(stringResource(R.string.favorite_section)) }
        }
        items(favoriteRoutes, key = { it.routeId }) { route ->
            FavoriteRouteItem(
                route = route,
                onClick = { onRouteClick(route) },
            )
            HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
        }
        items(favoriteNodes, key = { it.nodeId }) { node ->
            FavoriteNodeItem(
                node = node,
                onClick = { onNodeClick(node) },
            )
            HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = SRTheme.typography.bodyMSB,
        color = SRTheme.colors.black,
        modifier = Modifier.padding(horizontal = Spacing.space20, vertical = Spacing.space12),
    )
}

@Composable
private fun FavoriteRouteItem(
    route: BusRoute,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space8),
        ) {
            BusTypeLabel(type = route.routeType)
            Text(
                text = route.routeNo,
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.textPrimary,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space4),
        ) {
            Text(route.startNodeName, style = SRTheme.typography.bodyMM)
            Icon(
                imageVector = ImageVector.vectorResource(DR.drawable.ic_horizontal_arrow),
                contentDescription = null,
                modifier = Modifier.size(Spacing.space16),
                tint = SRTheme.colors.textPrimary,
            )
            Text(route.endNodeName, style = SRTheme.typography.bodyMM)
        }
    }
}

@Composable
private fun FavoriteNodeItem(
    node: BusNode,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
    ) {
        Text(
            text = node.nodeName,
            style = SRTheme.typography.bodyMM,
            color = SRTheme.colors.textPrimary,
        )
        node.nodeNo?.let { nodeNo ->
            Text(
                text = nodeNo,
                style = SRTheme.typography.bodySR,
                color = SRTheme.colors.textSecondary,
            )
        }
    }
}

@Preview(showBackground = true, name = "즐겨찾기 - 노선 + 정류장")
@Composable
private fun FavoriteSectionPreview() {
    SRTheme {
        FavoriteSection(
            favoriteRoutes = listOf(
                BusRoute("R001", "51", "일반버스", "노포동", "하단", CityCode.BUSAN),
                BusRoute("R002", "179", "급행버스", "기장", "사상", CityCode.BUSAN),
            ),
            favoriteNodes = listOf(
                BusNode("N001", "부산대학교앞", latitude = null, longitude = null, cityCode = CityCode.BUSAN, nodeNo = "12345"),
                BusNode("N002", "온천장역", latitude = null, longitude = null, cityCode = CityCode.BUSAN, nodeNo = null),
            ),
            onRouteClick = {},
            onNodeClick = {},
        )
    }
}
