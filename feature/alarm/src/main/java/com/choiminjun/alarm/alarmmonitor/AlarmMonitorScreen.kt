package com.choiminjun.alarm.alarmmonitor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.component.SRIconButton
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.alarm.R as HR
import com.choiminjun.designsystem.R as DesignR

private val NodeIconSize = 20.dp
private val NodeBadgeSize = 32.dp
private val NodeIconAreaWidth = 52.dp
private val NodeRowHeight = 64.dp

@Composable
internal fun AlarmMonitorRoute(
    onBackClick: () -> Unit,
    onAlarmStopped: () -> Unit,
    navigateToAlarmRing: () -> Unit,
    viewModel: AlarmMonitorViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            AlarmMonitorSideEffect.NavigateBack -> onBackClick()
            AlarmMonitorSideEffect.AlarmStopped -> onAlarmStopped()
            AlarmMonitorSideEffect.AlarmTriggered -> navigateToAlarmRing()
        }
    }

    AlarmMonitorScreen(
        state = state,
        onBackClick = { viewModel.onIntent(AlarmMonitorIntent.ClickBack) },
        onStopAlarm = { viewModel.onIntent(AlarmMonitorIntent.StopAlarm) },
    )
}

@Composable
private fun AlarmMonitorScreen(
    state: AlarmMonitorState,
    onBackClick: () -> Unit,
    onStopAlarm: () -> Unit,
) {
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
                .padding(horizontal = Spacing.space8, vertical = Spacing.space12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SRIconButton(
                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_arrow_left),
                contentDescription = stringResource(HR.string.back),
                onClick = onBackClick,
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onStopAlarm) {
                Text(
                    text = stringResource(HR.string.alarm_stop_button),
                    style = SRTheme.typography.bodyMM,
                    color = SRTheme.colors.blue50,
                )
            }
        }

        TrackingStatusText(
            nearestNodeName = state.nearestNodeName,
            remainingStops = state.remainingStops,
        )

        if (state.isLoadingNodes) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SRTheme.colors.blue50)
            }
        } else {
            RouteOverview(
                modifier = Modifier.weight(1f),
                routeNodeNames = state.routeNodeNames,
                boardingNodeName = state.alarmInfo.boardingNodeName,
                destNodeName = state.alarmInfo.destNodeName,
                nearestNodeName = state.nearestNodeName,
            )
        }
    }
}

@Composable
private fun TrackingStatusText(
    nearestNodeName: String?,
    remainingStops: Int?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.space20, vertical = Spacing.space8),
        verticalArrangement = Arrangement.spacedBy(Spacing.space4),
    ) {
        if (nearestNodeName == null) {
            Text(
                text = stringResource(HR.string.alarm_tracking_status),
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.textPrimary,
            )
        } else {
            Text(
                text = stringResource(HR.string.alarm_current_node_label, nearestNodeName),
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.textPrimary,
            )

            if (remainingStops != null) {
                Text(
                    text = stringResource(HR.string.alarm_remaining_stops, remainingStops),
                    style = SRTheme.typography.bodySR,
                    color = SRTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun RouteOverview(
    modifier: Modifier = Modifier,
    routeNodeNames: List<String>,
    boardingNodeName: String,
    destNodeName: String,
    nearestNodeName: String? = null,
) {
    val lineColor = SRTheme.colors.blue50
    val displayNodes = routeNodeNames.ifEmpty {
        listOfNotNull(boardingNodeName.ifBlank { null }, destNodeName.ifBlank { null })
    }
    val lastIndex = displayNodes.lastIndex

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.space20),
    ) {
        itemsIndexed(displayNodes, key = { _, name -> name }) { index, nodeName ->
            val badgeLabel = when (index) {
                0 -> stringResource(HR.string.alarm_boarding_label)
                lastIndex -> stringResource(HR.string.alarm_dest_label)
                else -> null
            }
            RouteNodeRow(
                nodeName = nodeName,
                badgeLabel = badgeLabel,
                isFirst = index == 0,
                isLast = index == lastIndex,
                isCurrentNode = nodeName == nearestNodeName,
            )
        }
    }
}

@Composable
private fun RouteNodeRow(
    nodeName: String,
    badgeLabel: String?,
    isFirst: Boolean,
    isLast: Boolean,
    isCurrentNode: Boolean,
) {
    val lineColor = SRTheme.colors.blue50
    val iconColor = SRTheme.colors.coolNeutral70
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(NodeRowHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(NodeIconAreaWidth)
                .fillMaxHeight()
                .drawBehind {
                    val iconCenterX = size.width / 2f
                    val centerY = size.height / 2f
                    val halfIcon = if (badgeLabel != null || isCurrentNode) NodeBadgeSize.toPx() / 2f else NodeIconSize.toPx() / 2f
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
            contentAlignment = Alignment.Center,
        ) {
            when {
                isCurrentNode -> Icon(
                    imageVector = ImageVector.vectorResource(DesignR.drawable.ic_bus),
                    contentDescription = null,
                    tint = SRTheme.colors.blue50,
                    modifier = Modifier.size(NodeBadgeSize),
                )
                badgeLabel != null -> Box(
                    modifier = Modifier
                        .size(NodeBadgeSize)
                        .border(1.dp, iconColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = badgeLabel,
                        style = SRTheme.typography.bodyXSSB,
                        color = iconColor,
                    )
                }
                else -> Icon(
                    imageVector = ImageVector.vectorResource(DesignR.drawable.ic_chevron_down_circle),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(NodeIconSize),
                )
            }
        }
        Text(
            modifier = Modifier.weight(1f),
            text = nodeName,
            style = SRTheme.typography.bodyMM,
            color = SRTheme.colors.textPrimary,
        )
    }
}

private val previewAlarmInfo = AlarmInfo(
    routeNo = "51",
    boardingNodeName = "부산대학교앞",
    destNodeName = "하단",
    stopsBeforeAlarm = 2,
)

private val previewRouteNodes = listOf(
    "부산대학교앞", "온천장역", "부암시장", "동래역", "명장동",
    "수영교차로", "광안리해수욕장", "민락동", "하단",
)

@Preview(showBackground = true, name = "1_Loading")
@Composable
private fun AlarmMonitorLoadingPreview() {
    SRTheme {
        AlarmMonitorScreen(
            state = AlarmMonitorState(
                alarmInfo = previewAlarmInfo,
                isLoadingNodes = true,
            ),
            onBackClick = {},
            onStopAlarm = {},
        )
    }
}

@Preview(showBackground = true, name = "2_Tracking")
@Composable
private fun AlarmMonitorTrackingPreview() {
    SRTheme {
        AlarmMonitorScreen(
            state = AlarmMonitorState(
                alarmInfo = previewAlarmInfo,
                routeNodeNames = previewRouteNodes,
                isLoadingNodes = false,
                nearestNodeName = null,
            ),
            onBackClick = {},
            onStopAlarm = {},
        )
    }
}

@Preview(showBackground = true, name = "3_Location Found")
@Composable
private fun AlarmMonitorLocationFoundPreview() {
    SRTheme {
        AlarmMonitorScreen(
            state = AlarmMonitorState(
                alarmInfo = previewAlarmInfo,
                routeNodeNames = previewRouteNodes,
                isLoadingNodes = false,
                nearestNodeName = "동래역",
                remainingStops = 4,
            ),
            onBackClick = {},
            onStopAlarm = {},
        )
    }
}

@Preview(showBackground = true, name = "4_Almost Arrived")
@Composable
private fun AlarmMonitorAlmostArrivedPreview() {
    SRTheme {
        AlarmMonitorScreen(
            state = AlarmMonitorState(
                alarmInfo = previewAlarmInfo,
                routeNodeNames = previewRouteNodes,
                isLoadingNodes = false,
                nearestNodeName = "민락동",
                remainingStops = 1,
            ),
            onBackClick = {},
            onStopAlarm = {},
        )
    }
}
