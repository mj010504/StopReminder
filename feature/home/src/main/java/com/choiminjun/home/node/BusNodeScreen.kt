package com.choiminjun.home.node

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.designsystem.R
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.navigation.HomeGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusNodeScreenState(
    val isLoading: Boolean = false,
    val routes: List<BusRoute> = emptyList(),
)

@HiltViewModel
class BusNodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BusNodeScreenState())
    val state = _state.asStateFlow()

    init {
        val nodeId = savedStateHandle.toRoute<HomeGraph.BusNodeRoute>().nodeId
        loadRoutes(nodeId)
    }

    private fun loadRoutes(nodeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val routes = runCatching { busRepository.getRoutesByNode(CityCode.BUSAN, nodeId) }
                .getOrElse { emptyList() }
            _state.update { it.copy(isLoading = false, routes = routes) }
        }
    }
}

@Composable
internal fun BusNodeScreen(
    onBackClick: () -> Unit,
    onAlarmClick: (routeId: String) -> Unit,
    viewModel: BusNodeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = "뒤로가기",
                tint = SRTheme.colors.icon,
                modifier = Modifier.clickable { onBackClick() },
            )
        }
        HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.routes, key = { it.routeId }) { route ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = route.routeNo,
                                style = SRTheme.typography.bodyMM,
                                color = SRTheme.colors.textPrimary,
                            )
                            Text(
                                text = "${route.startNodeName} → ${route.endNodeName}",
                                style = SRTheme.typography.bodySR,
                                color = SRTheme.colors.textSecondary,
                            )
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_bell),
                            contentDescription = "알람 설정",
                            tint = SRTheme.colors.icon,
                            modifier = Modifier.clickable { onAlarmClick(route.routeId) },
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = SRTheme.colors.coolNeutral95,
                        modifier = Modifier.padding(horizontal = Spacing.space20),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BusNodeScreenPreview() {
    SRTheme {
        val previewState = BusNodeScreenState(
            routes = listOf(
                BusRoute(
                    routeId = "R001",
                    routeNo = "51",
                    routeType = "일반",
                    startNodeName = "노포동",
                    endNodeName = "하단",
                    cityCode = CityCode.BUSAN,
                ),
                BusRoute(
                    routeId = "R002",
                    routeNo = "179",
                    routeType = "일반",
                    startNodeName = "기장",
                    endNodeName = "사상",
                    cityCode = CityCode.BUSAN,
                ),
                BusRoute(
                    routeId = "R003",
                    routeNo = "1001",
                    routeType = "급행",
                    startNodeName = "해운대",
                    endNodeName = "서면",
                    cityCode = CityCode.BUSAN,
                ),
            ),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SRTheme.colors.background)
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            HorizontalDivider(thickness = 1.dp, color = SRTheme.colors.coolNeutral95)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(previewState.routes, key = { it.routeId }) { route ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = route.routeNo,
                                style = SRTheme.typography.bodyMM,
                                color = SRTheme.colors.textPrimary,
                            )
                            Text(
                                text = "${route.startNodeName} → ${route.endNodeName}",
                                style = SRTheme.typography.bodySR,
                                color = SRTheme.colors.textSecondary,
                            )
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_bell),
                            contentDescription = "알람 설정",
                            tint = SRTheme.colors.icon,
                            modifier = Modifier.clickable { },
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = SRTheme.colors.coolNeutral95,
                        modifier = Modifier.padding(horizontal = Spacing.space20),
                    )
                }
            }
        }
    }
}
