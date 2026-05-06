package com.choiminjun.home.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.R
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.home.home.component.SearchField
import com.choiminjun.home.home.component.SearchResult

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToBusRoute: (String) -> Unit,
    navigateToBusNode: (String) -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is HomeSideEffect.NavigateToBusRoute -> navigateToBusRoute(effect.routeId)
            is HomeSideEffect.NavigateToBusNode -> navigateToBusNode(effect.nodeId)
        }
    }

    HomeScreen(
        state = state,
        onBackClick = { viewModel.onIntent(HomeIntent.ClickBack) },
        onQueryChange = { query -> viewModel.onIntent(HomeIntent.UpdateQuery(query)) },
        onSearchFocused = { viewModel.onIntent(HomeIntent.FocusSearch) },
        onBusRouteClick = { routeId -> viewModel.onIntent(HomeIntent.ClickBusRoute(routeId)) },
        onBusNodeClick = { nodeId -> viewModel.onIntent(HomeIntent.ClickBusNode(nodeId)) },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onBusRouteClick: (String) -> Unit,
    onBusNodeClick: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible

    val handleBack: () -> Unit = {
        if (imeVisible) keyboardController?.hide() else onBackClick()
    }

    BackHandler(enabled = imeVisible || state.isSearching) {
        handleBack()
    }

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
            horizontalArrangement = Arrangement.spacedBy(Spacing.space16),
        ) {
            Icon(
                imageVector = if (state.isSearching) {
                    ImageVector.vectorResource(R.drawable.ic_arrow_left)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_menu)
                },
                contentDescription = if (state.isSearching) "뒤로가기" else "메뉴",
                tint = SRTheme.colors.icon,
                modifier = Modifier.clickable { handleBack() },
            )
            SearchField(
                value = state.searchQuery,
                onValueChange = onQueryChange,
                onFocused = onSearchFocused,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = SRTheme.colors.coolNeutral95,
        )

        if (state.isSearching) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = SRTheme.colors.blue50)
                }
            } else {
                SearchResult(
                    modifier = Modifier.weight(1f),
                    query = state.searchQuery,
                    routes = state.searchedRoutes,
                    nodes = state.searchedNodes,
                    onBusRouteClick = onBusRouteClick,
                    onBusNodeClick = onBusNodeClick,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    SRTheme {
        HomeScreen(
            state = HomeState(),
            onBackClick = {},
            onQueryChange = {},
            onSearchFocused = {},
            onBusRouteClick = {},
            onBusNodeClick = {},
        )
    }
}
