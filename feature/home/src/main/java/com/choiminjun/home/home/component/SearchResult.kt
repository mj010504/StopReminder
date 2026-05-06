package com.choiminjun.home.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.choiminjun.home.R
import com.choiminjun.designsystem.R as DesignSystemR

@Composable
internal fun SearchResult(
    query: String,
    routes: List<BusRoute>,
    nodes: List<BusNode>,
    onBusRouteClick: (String) -> Unit,
    onBusNodeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(SRTheme.colors.background),
    ) {
        when {
            query.isBlank() -> {
                // TODO: 최근 검색어 불러오기
            }

            routes.isEmpty() && nodes.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(Spacing.space20),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.search_empty_result),
                            style = SRTheme.typography.bodyMR,
                            color = SRTheme.colors.textSecondary,
                        )
                    }
                }
            }

            else -> {
                if (routes.isNotEmpty()) {
                    item { SearchSectionHeader(title = stringResource(R.string.search_section_bus_route)) }
                    items(routes, key = { it.routeId }) { route ->
                        BusRouteResultItem(
                            route = route,
                            onBusRouteClick = { onBusRouteClick(route.routeId) },
                        )
                    }
                }
                if (nodes.isNotEmpty()) {
                    item { SearchSectionHeader(title = stringResource(R.string.search_section_bus_stop)) }
                    items(nodes, key = { it.nodeId }) { node ->
                        BusNodeResultItem(
                            node = node,
                            onBusNodeClick = { onBusNodeClick(node.nodeId) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSectionHeader(title: String) {
    Text(
        text = title,
        style = SRTheme.typography.bodySM,
        color = SRTheme.colors.textSecondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.space20, vertical = Spacing.space8),
    )
}

@Composable
private fun BusRouteResultItem(
    route: BusRoute,
    onBusRouteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBusRouteClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space12),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_bus),
                contentDescription = null,
                tint = SRTheme.colors.blue50,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Text(
                    text = route.routeNo,
                    style = SRTheme.typography.bodyMM,
                    color = SRTheme.colors.textPrimary,
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = SRTheme.colors.coolNeutral95,
        modifier = Modifier.padding(horizontal = Spacing.space20),
    )
}

@Composable
private fun BusNodeResultItem(
    node: BusNode,
    onBusNodeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBusNodeClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space12),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_bus_stop),
                contentDescription = null,
                tint = SRTheme.colors.icon,
                modifier = Modifier.size(20.dp),
            )
            Column {
                Text(
                    text = node.nodeName,
                    style = SRTheme.typography.bodyMM,
                    color = SRTheme.colors.textPrimary,
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = SRTheme.colors.coolNeutral95,
        modifier = Modifier.padding(horizontal = Spacing.space20),
    )
}

@Preview(showBackground = true, name = "검색 결과")
@Composable
private fun SearchResultPreview() {
    val previewRoutes = listOf(
        BusRoute(
            routeId = "1",
            routeNo = "51",
            routeType = "",
            startNodeName = "노포동",
            endNodeName = "하단",
        ),
        BusRoute(
            routeId = "2",
            routeNo = "179",
            routeType = "",
            startNodeName = "기장",
            endNodeName = "사상",
        ),
    )

    val previewNodes = listOf(
        BusNode(
            nodeId = "N1",
            nodeName = "부산대학교",
            latitude = 35.23,
            longitude = 129.08,
        ),
        BusNode(
            nodeId = "N2",
            nodeName = "서면역",
            latitude = 35.15,
            longitude = 129.05,
        ),
    )

    SRTheme {
        SearchResult(
            query = "51",
            routes = previewRoutes,
            nodes = previewNodes,
            onBusRouteClick = {},
            onBusNodeClick = {},
        )
    }
}

@Preview(showBackground = true, name = "검색 결과 - 결과 없음")
@Composable
private fun SearchResultEmptyPreview() {
    SRTheme {
        SearchResult(
            query = "존재하지않는노선",
            routes = emptyList(),
            nodes = emptyList(),
            onBusRouteClick = {},
            onBusNodeClick = {},
        )
    }
}
