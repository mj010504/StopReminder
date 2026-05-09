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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.R
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.designsystem.util.noRippleClickable
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode

@Composable
internal fun BusNodeRoute(
    onBackClick: () -> Unit,
    onAlarmClick: (routeId: String) -> Unit,
    navigateToBusRoute: (routeId: String, routeNo: String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: BusNodeViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            BusNodeSideEffect.NavigateBack -> onBackClick()
            is BusNodeSideEffect.NavigateToAlarm -> onAlarmClick(effect.routeId)
            is BusNodeSideEffect.NavigateToBusRoute -> navigateToBusRoute(effect.routeId, effect.routeNo)
        }
    }

    BusNodeScreen(
        state = state,
        onBackClick = { viewModel.onIntent(BusNodeIntent.ClickBack) },
        onAlarmClick = { routeId -> viewModel.onIntent(BusNodeIntent.ClickAlarm(routeId)) },
        onRouteClick = { routeId, routeNo -> viewModel.onIntent(BusNodeIntent.ClickBusRoute(routeId, routeNo)) },
        onHomeClick = navigateToHome,
    )
}

@Composable
private fun BusNodeScreen(
    state: BusNodeState,
    onBackClick: () -> Unit,
    onAlarmClick: (routeId: String) -> Unit,
    onRouteClick: (routeId: String, routeNo: String) -> Unit,
    onHomeClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SRTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space12),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = "뒤로가기",
                tint = SRTheme.colors.icon,
                modifier = Modifier.noRippleClickable { onBackClick() },
            )
            Text(
                text = state.nodeName,
                style = SRTheme.typography.bodyXMM,
                color = SRTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )
            // TODO: 즐겨찾기 기능 구현
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = "즐겨찾기",
                tint = SRTheme.colors.icon,
                modifier = Modifier.noRippleClickable { },
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_home),
                contentDescription = "홈",
                tint = SRTheme.colors.icon,
                modifier = Modifier.noRippleClickable { onHomeClick() },
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SRTheme.colors.blue50)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
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
                        onAlarmClick = { onAlarmClick(route.routeId) },
                        onRouteClick = { onRouteClick(route.routeId, route.routeNo) },
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
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bell),
            contentDescription = "알림",
            tint = SRTheme.colors.icon,
            modifier = Modifier.clickable { onAlarmClick() },
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
            onBackClick = {},
            onAlarmClick = {},
            onRouteClick = { _, _ -> },
            onHomeClick = {},
        )
    }
}
