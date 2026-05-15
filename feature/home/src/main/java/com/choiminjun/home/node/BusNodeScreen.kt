package com.choiminjun.home.node

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.R
import com.choiminjun.designsystem.component.SRIconButton
import com.choiminjun.designsystem.component.SRSnackbar
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.choiminjun.home.R as HR

@Composable
internal fun BusNodeRoute(
    onBackClick: () -> Unit,
    onAlarmClick: (routeId: String, routeNo: String) -> Unit,
    navigateToBusRoute:
    (routeId: String, routeNo: String, routeType: String, startNodeName: String, endNodeName: String, cityCode: String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: BusNodeViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val favoriteAddedMessage = stringResource(HR.string.favorite_added)
    val favoriteRemovedMessage = stringResource(HR.string.favorite_removed)
    val scope = rememberCoroutineScope()
    var snackbarJob: Job? = null

    viewModel.collectSideEffect { effect ->
        when (effect) {
            BusNodeSideEffect.NavigateBack -> onBackClick()
            is BusNodeSideEffect.NavigateToAlarm -> onAlarmClick(effect.routeId, effect.routeNo)
            is BusNodeSideEffect.NavigateToBusRoute -> navigateToBusRoute(
                effect.routeId,
                effect.routeNo,
                effect.routeType,
                effect.startNodeName,
                effect.endNodeName,
                effect.cityCode,
            )

            is BusNodeSideEffect.ShowSnackbar -> {
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

    BusNodeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = { viewModel.onIntent(BusNodeIntent.ClickBack) },
        onAlarmClick = { routeId, routeNo -> viewModel.onIntent(BusNodeIntent.ClickAlarm(routeId, routeNo)) },
        onRouteClick = { route -> viewModel.onIntent(BusNodeIntent.ClickBusRoute(route)) },
        onHomeClick = navigateToHome,
        onFavoriteClick = { viewModel.onIntent(BusNodeIntent.ToggleFavorite) },
    )
}

@Composable
private fun BusNodeScreen(
    state: BusNodeState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAlarmClick: (routeId: String, routeNo: String) -> Unit,
    onRouteClick: (BusRoute) -> Unit,
    onHomeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = SRTheme.colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.space20, vertical = Spacing.space16)
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
                    text = state.nodeName,
                    style = SRTheme.typography.bodyXMM,
                    color = SRTheme.colors.textPrimary,
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
                    contentDescription = "홈",
                    onClick = { onHomeClick() },
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 20.dp),
                snackbar = { SRSnackbar(snackbarData = it) },
            )
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
                    state.nodeNo?.let { nodeNo ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.space16),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = nodeNo,
                                style = SRTheme.typography.bodyMM,
                                color = SRTheme.colors.textSecondary,
                            )
                        }
                        HorizontalDivider(thickness = Spacing.space6, color = SRTheme.colors.coolNeutral95)
                    }
                }

                items(state.routes, key = { it.routeId }) { route ->
                    BusNodeRouteItem(
                        route = route,
                        onAlarmClick = { onAlarmClick(route.routeId, route.routeNo) },
                        onRouteClick = { onRouteClick(route) },
                    )
                }

                item {
                    Spacer(Modifier.height(Spacing.space64))
                }
            }
        }
    }
}

@Composable
private fun BusNodeRouteItem(
    route: BusRoute,
    onAlarmClick: () -> Unit,
    onRouteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRouteClick() }
            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.routeNo,
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.blue50,
            )
            Text(
                text = "${route.startNodeName} → ${route.endNodeName}",
                style = SRTheme.typography.bodySR,
                color = SRTheme.colors.textSecondary,
            )
        }
        SRIconButton(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bell),
            contentDescription = "알림",
            onClick = { onAlarmClick() },
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = SRTheme.colors.coolNeutral95,
        modifier = Modifier.padding(horizontal = Spacing.space20),
    )
}

@Preview(showBackground = true)
@Composable
private fun BusNodeScreenPreview() {
    SRTheme {
        BusNodeScreen(
            state = BusNodeState(
                nodeName = "부산대학교앞",
                nodeNo = "12345",
                routes = listOf(
                    BusRoute("R001", "51", "일반", "노포동", "하단", CityCode.BUSAN),
                    BusRoute("R002", "179", "일반", "기장", "사상", CityCode.BUSAN),
                    BusRoute("R003", "1001", "급행", "해운대", "서면", CityCode.BUSAN),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onAlarmClick = { _, _ -> },
            onRouteClick = { },
            onHomeClick = {},
            onFavoriteClick = {},
        )
    }
}
