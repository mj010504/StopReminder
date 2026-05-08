package com.choiminjun.home.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.navigation.HomeGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusRouteScreenState(
    val isLoading: Boolean = false,
    val nodes: List<BusNode> = emptyList(),
)

@HiltViewModel
class BusRouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BusRouteScreenState())
    val state = _state.asStateFlow()

    init {
        val routeId = savedStateHandle.toRoute<HomeGraph.BusRouteRoute>().routeId
        loadNodes(routeId)
    }

    private fun loadNodes(routeId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val nodes = runCatching { busRepository.getNodesByRoute(CityCode.BUSAN, routeId) }
                .getOrElse { emptyList() }
            _state.update { it.copy(isLoading = false, nodes = nodes) }
        }
    }
}

@Composable
internal fun BusRouteScreen(
    onBackClick: () -> Unit,
    viewModel: BusRouteViewModel = hiltViewModel(),
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
                items(state.nodes, key = { it.nodeId }) { node ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
                    ) {
                        Text(
                            text = node.nodeName,
                            style = SRTheme.typography.bodyMM,
                            color = SRTheme.colors.textPrimary,
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
private fun BusRouteScreenPreview() {
    SRTheme {
        val previewState = BusRouteScreenState(
            nodes = listOf(
                BusNode(nodeId = "BSB001", nodeName = "부산대학교앞", latitude = 35.23, longitude = 129.08, cityCode = CityCode.BUSAN),
                BusNode(nodeId = "BSB002", nodeName = "온천장역", latitude = 35.22, longitude = 129.07, cityCode = CityCode.BUSAN),
                BusNode(nodeId = "BSB003", nodeName = "동래역", latitude = 35.20, longitude = 129.06, cityCode = CityCode.BUSAN),
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
                items(previewState.nodes, key = { it.nodeId }) { node ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
                    ) {
                        Text(
                            text = node.nodeName,
                            style = SRTheme.typography.bodyMM,
                            color = SRTheme.colors.textPrimary,
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
