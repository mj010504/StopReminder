package com.choiminjun.home.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.R
import com.choiminjun.designsystem.component.SRIconButton
import com.choiminjun.designsystem.component.SRSnackbar
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.CityCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.choiminjun.home.R as HR

private val StopItemHeight: Dp = 68.dp
private val StopIconSize: Dp = 24.dp
private val StopIconAreaWidth: Dp = Spacing.space20 + StopIconSize + Spacing.space12

@Composable
internal fun BusRouteRoute(
    onBackClick: () -> Unit,
    navigateToBusNode: (nodeId: String, nodeName: String, nodeNo: String?, cityCode: String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: BusRouteViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val favoriteAddedMessage = stringResource(HR.string.favorite_added)
    val favoriteRemovedMessage = stringResource(HR.string.favorite_removed)
    val scope = rememberCoroutineScope()
    var snackbarJob: Job? = null

    viewModel.collectSideEffect { effect ->
        when (effect) {
            BusRouteSideEffect.NavigateBack -> onBackClick()
            is BusRouteSideEffect.NavigateToBusNode -> navigateToBusNode(
                effect.nodeId,
                effect.nodeName,
                effect.nodeNo,
                effect.cityCode,
            )

            is BusRouteSideEffect.ShowSnackbar -> {
                snackbarJob?.cancel()
                snackbarJob = scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        if (effect.added) favoriteAddedMessage else favoriteRemovedMessage,
                    )
                }
            }
        }
    }

    BusRouteScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { viewModel.onIntent(BusRouteIntent.ClickBack) },
        onNodeClick = { node -> viewModel.onIntent(BusRouteIntent.ClickBusNode(node)) },
        onFavoriteClick = { viewModel.onIntent(BusRouteIntent.ToggleFavorite) },
        onHomeClick = navigateToHome,
    )
}

@Composable
private fun BusRouteScreen(
    state: BusRouteState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onNodeClick: (BusNode) -> Unit,
    onFavoriteClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = SRTheme.colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.space20, vertical = Spacing.space12)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.space12),
            ) {
                SRIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                    contentDescription = "뒤로가기",
                    onClick = { onBackClick() },
                )
                Text(
                    text = state.routeNo,
                    style = SRTheme.typography.bodyXMM,
                    color = SRTheme.colors.blue50,
                    modifier = Modifier.weight(1f),
                )
                SRIconButton(
                    imageVector = if (state.isFavorite) {
                        ImageVector.vectorResource(R.drawable.ic_star_fill)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_star)
                    },
                    contentDescription = "즐겨찾기",
                    tint = if (state.isFavorite) SRTheme.colors.yellow else Color.Unspecified,
                    onClick = onFavoriteClick,
                )
                SRIconButton(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_home),
                    contentDescription = "홈으로",
                    onClick = { onHomeClick() },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                SRSnackbar(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    snackbarData = it,
                )
            }
        },
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SRTheme.colors.blue50)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = listState,
            ) {
                item {
                    val firstNode = state.nodes.firstOrNull()
                    val lastNode = state.nodes.lastOrNull()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.space16),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
                    ) {
                        Text(
                            text = firstNode?.cityCode?.label ?: "",
                            style = SRTheme.typography.bodyXMM,
                            color = SRTheme.colors.textSecondary,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.space4),
                        ) {
                            Text(
                                text = firstNode?.nodeName ?: "",
                                style = SRTheme.typography.bodyMM,
                                color = SRTheme.colors.textPrimary,
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_horizontal_arrow),
                                contentDescription = null,
                                modifier = Modifier.size(Spacing.space16),
                                tint = SRTheme.colors.textPrimary,
                            )
                            Text(
                                text = lastNode?.nodeName ?: "",
                                style = SRTheme.typography.bodyMM,
                                color = SRTheme.colors.textPrimary,
                            )
                        }
                    }
                    HorizontalDivider(thickness = Spacing.space6, color = SRTheme.colors.coolNeutral95)
                }

                itemsIndexed(
                    items = state.nodes,
                    key = { _, node -> node.nodeId },
                ) { index, node ->
                    Box {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
                        BusRouteStopItem(
                            nodeName = node.nodeName,
                            nodeNo = node.nodeNo,
                            isFirst = index == 0,
                            isLast = index == state.nodes.lastIndex,
                            lineColor = SRTheme.colors.blue50,
                            onClick = { onNodeClick(node) },
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
private fun BusRouteStopItem(
    nodeName: String,
    nodeNo: String?,
    isFirst: Boolean,
    isLast: Boolean,
    lineColor: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(StopItemHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(StopIconAreaWidth)
                .fillMaxHeight()
                .drawBehind {
                    val iconCenterX = Spacing.space20.toPx() + StopIconSize.toPx() / 2f
                    val centerY = size.height / 2f
                    val halfIcon = StopIconSize.toPx() / 2f
                    val strokeWidth = 2.dp.toPx()

                    if (!isFirst) {
                        drawLine(
                            color = lineColor,
                            start = Offset(iconCenterX, 0f),
                            end = Offset(iconCenterX, centerY - halfIcon),
                            strokeWidth = strokeWidth,
                        )
                    }

                    if (!isLast) {
                        drawLine(
                            color = lineColor,
                            start = Offset(iconCenterX, centerY + halfIcon),
                            end = Offset(iconCenterX, size.height),
                            strokeWidth = strokeWidth,
                        )
                    }
                },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_down_circle),
                contentDescription = null,
                tint = SRTheme.colors.coolNeutral70,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = Spacing.space20)
                    .size(StopIconSize),
            )
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.space4)) {
            Text(
                text = nodeName,
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(end = Spacing.space20),
            )

            nodeNo?.let { nodeNo ->
                Text(
                    text = nodeNo,
                    style = SRTheme.typography.bodyXSR,
                    color = SRTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BusRouteScreenPreview() {
    val nodes = listOf(
        BusNode("BSB001", "노포동", latitude = 35.27, longitude = 129.09, cityCode = CityCode.BUSAN, nodeNo = "12345"),
        BusNode("BSB002", "부산대학교앞", latitude = 35.23, longitude = 129.08, cityCode = CityCode.BUSAN),
        BusNode("BSB003", "온천장역", latitude = 35.22, longitude = 129.07, cityCode = CityCode.BUSAN),
        BusNode("BSB004", "동래역", latitude = 35.20, longitude = 129.06, cityCode = CityCode.BUSAN, nodeNo = "16630"),
        BusNode("BSB005", "하단", latitude = 35.10, longitude = 128.97, cityCode = CityCode.BUSAN),
    )
    SRTheme {
        BusRouteScreen(
            state = BusRouteState(routeNo = "51", nodes = nodes),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onNodeClick = { },
            onFavoriteClick = {},
            onHomeClick = {},
        )
    }
}
