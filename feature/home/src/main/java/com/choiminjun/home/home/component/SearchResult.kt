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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch
import com.choiminjun.domain.model.search.toBusNode
import com.choiminjun.domain.model.search.toBusRoute
import com.choiminjun.home.R
import com.choiminjun.home.home.SearchTab
import com.choiminjun.designsystem.R as DR

@Composable
internal fun SearchResult(
    query: String,
    routes: List<BusRoute>,
    nodes: List<BusNode>,
    selectedTab: SearchTab,
    recentRouteSearches: List<RecentRouteSearch>,
    recentNodeSearches: List<RecentNodeSearch>,
    onBusRouteClick: (BusRoute) -> Unit,
    onBusNodeClick: (BusNode) -> Unit,
    onRecentRouteSearchDelete: (String) -> Unit,
    onRecentNodeSearchDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visibleRoutes = if (selectedTab == SearchTab.BUS) routes else emptyList()
    val visibleNodes = if (selectedTab == SearchTab.STOP) nodes else emptyList()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(SRTheme.colors.background),
    ) {
        when {
            query.isBlank() -> {
                if ((selectedTab == SearchTab.BUS && recentRouteSearches.isEmpty()) ||
                    (selectedTab == SearchTab.STOP && recentNodeSearches.isEmpty())
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(Spacing.space20),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                modifier = Modifier.imePadding(),
                                text = stringResource(R.string.search_recent_empty),
                                style = SRTheme.typography.bodyMR,
                                color = SRTheme.colors.textSecondary,
                            )
                        }
                    }
                } else {
                    when (selectedTab) {
                        SearchTab.BUS -> {
                            items(recentRouteSearches, key = { "route_${it.routeId}" }) { item ->
                                RecentRouteSearchItem(
                                    item = item,
                                    onItemClick = {
                                        onBusRouteClick(item.toBusRoute())
                                    },
                                    onDeleteClick = { onRecentRouteSearchDelete(item.routeId) },
                                )
                            }
                        }

                        SearchTab.STOP -> {
                            items(recentNodeSearches, key = { "node_${it.nodeId}" }) { item ->
                                RecentNodeSearchItem(
                                    item = item,
                                    onItemClick = {
                                        onBusNodeClick(item.toBusNode())
                                    },
                                    onDeleteClick = { onRecentNodeSearchDelete(item.nodeId) },
                                )
                            }
                        }
                    }
                }
            }

            visibleRoutes.isEmpty() && visibleNodes.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(Spacing.space28),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.imePadding(),
                            text = stringResource(R.string.search_empty_result),
                            style = SRTheme.typography.bodyMR,
                            color = SRTheme.colors.textSecondary,
                        )
                    }
                }
            }

            else -> {
                val groupedRoutes = visibleRoutes.groupBy { it.cityCode }
                val groupedNodes = visibleNodes.groupBy { it.cityCode }

                groupedRoutes.forEach { (cityCode, routeGroup) ->
                    item(key = "route_header_${cityCode.code}") {
                        CityHeader(label = cityCode.label)
                    }
                    items(routeGroup, key = { "route_${it.routeId}" }) { route ->
                        BusRouteResultItem(
                            route = route,
                            query = query,
                            onBusRouteClick = { onBusRouteClick(route) },
                        )
                    }
                }

                groupedNodes.forEach { (cityCode, nodeGroup) ->
                    item(key = "node_header_${cityCode.code}") {
                        CityHeader(label = cityCode.label)
                    }
                    items(nodeGroup, key = { "node_${it.nodeId}" }) { node ->
                        BusNodeResultItem(
                            node = node,
                            query = query,
                            onBusNodeClick = { onBusNodeClick(node) },
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(Spacing.space64))
                }
            }
        }
    }
}

@Composable
private fun CityHeader(label: String) {
    Text(
        text = label,
        style = SRTheme.typography.bodyXMM,
        color = SRTheme.colors.textPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
    )
}

@Composable
private fun BusRouteResultItem(
    route: BusRoute,
    query: String,
    onBusRouteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBusRouteClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space8),
        ) {
            BusTypeLabel(type = route.routeType)
            HighlightedText(
                text = route.routeNo,
                query = query,
                style = SRTheme.typography.bodyMM,
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

    HorizontalDivider(
        thickness = 1.dp,
        color = SRTheme.colors.coolNeutral95,
    )
}

@Composable
private fun BusNodeResultItem(
    node: BusNode,
    query: String,
    onBusNodeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBusNodeClick)
            .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
    ) {
        HighlightedText(
            text = node.nodeName,
            query = query,
            style = SRTheme.typography.bodyMM,
        )

        node.nodeNo?.let { nodeNo ->
            Text(
                text = nodeNo,
                style = SRTheme.typography.bodySR,
                color = SRTheme.colors.textSecondary,
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = SRTheme.colors.coolNeutral95,
        modifier = Modifier.padding(horizontal = Spacing.space20),
    )
}

@Composable
private fun HighlightedText(
    text: String,
    query: String,
    style: TextStyle,
) {
    val highlightColor = SRTheme.colors.blue50
    val defaultColor = SRTheme.colors.textPrimary
    val annotatedString = remember(text, query, defaultColor) {
        buildAnnotatedString {
            if (query.isBlank()) {
                withStyle(SpanStyle(color = defaultColor)) { append(text) }
                return@buildAnnotatedString
            }
            val lowerText = text.lowercase()
            val lowerQuery = query.lowercase()
            var cursor = 0
            while (cursor < text.length) {
                val matchIndex = lowerText.indexOf(lowerQuery, cursor)
                if (matchIndex == -1) {
                    withStyle(SpanStyle(color = defaultColor)) { append(text.substring(cursor)) }
                    break
                }
                if (matchIndex > cursor) {
                    withStyle(SpanStyle(color = defaultColor)) {
                        append(text.substring(cursor, matchIndex))
                    }
                }
                withStyle(SpanStyle(color = highlightColor)) {
                    append(text.substring(matchIndex, matchIndex + query.length))
                }
                cursor = matchIndex + query.length
            }
        }
    }
    Text(text = annotatedString, style = style)
}

@Preview(showBackground = true, name = "검색 결과 - 버스 탭")
@Composable
private fun SearchResultBusTabPreview() {
    val previewRoutes = listOf(
        BusRoute(
            routeId = "1",
            routeNo = "51",
            routeType = "일반",
            startNodeName = "노포동",
            endNodeName = "하단",
            cityCode = CityCode.BUSAN,
        ),
        BusRoute(
            routeId = "2",
            routeNo = "사하구5",
            routeType = "급행",
            startNodeName = "기장",
            endNodeName = "사상",
            cityCode = CityCode.BUSAN,
        ),
    )

    SRTheme {
        SearchResult(
            query = "5",
            routes = previewRoutes,
            nodes = emptyList(),
            selectedTab = SearchTab.BUS,
            recentRouteSearches = emptyList(),
            recentNodeSearches = emptyList(),
            onBusRouteClick = { },
            onBusNodeClick = { },
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
        )
    }
}

@Preview(showBackground = true, name = "검색 결과 - 정류장 탭")
@Composable
private fun SearchResultStopTabPreview() {
    val previewNodes = listOf(
        BusNode(
            nodeId = "N1",
            nodeName = "부산대학교",
            latitude = 35.23,
            longitude = 129.08,
            cityCode = CityCode.BUSAN,
            nodeNo = "10068",
        ),
        BusNode(
            nodeId = "N2",
            nodeName = "부산역",
            latitude = 35.15,
            longitude = 129.05,
            cityCode = CityCode.BUSAN,
        ),
    )

    SRTheme {
        SearchResult(
            query = "부산",
            routes = emptyList(),
            nodes = previewNodes,
            selectedTab = SearchTab.STOP,
            recentRouteSearches = emptyList(),
            recentNodeSearches = emptyList(),
            onBusRouteClick = { },
            onBusNodeClick = { },
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
        )
    }
}

@Preview(showBackground = true, name = "검색 결과 - 결과 없음")
@Composable
private fun SearchResultEmptyPreview() {
    SRTheme {
        SearchResult(
            query = "존재하지 않는 노선",
            routes = emptyList(),
            nodes = emptyList(),
            selectedTab = SearchTab.BUS,
            recentRouteSearches = emptyList(),
            recentNodeSearches = emptyList(),
            onBusRouteClick = { },
            onBusNodeClick = { },
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
        )
    }
}
