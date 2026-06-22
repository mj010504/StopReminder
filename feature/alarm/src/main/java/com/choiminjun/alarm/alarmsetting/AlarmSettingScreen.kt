package com.choiminjun.alarm.alarmsetting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.alarm.R
import com.choiminjun.designsystem.component.SRIconButton
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.designsystem.R as DesignR

private val StopItemHeight: Dp = 68.dp
private val StopIconSize: Dp = 24.dp
private val StopIconAreaWidth: Dp = Spacing.space20 + StopIconSize + Spacing.space12

@Composable
internal fun AlarmSettingRoute(
    onBackClick: () -> Unit,
    onAlarmSet: (routeNo: String, nodeName: String) -> Unit,
    viewModel: AlarmSettingViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    var pendingAlarm by remember { mutableStateOf<AlarmSettingSideEffect.AlarmConfirmed?>(null) }
    var showPermissionSheet by remember { mutableStateOf(false) }
    var showLocationPermissionSheet by remember { mutableStateOf(false) }

    val notificationPermLauncher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        val alarm = pendingAlarm
        pendingAlarm = null
        if (granted) {
            alarm?.let { effect ->
                onAlarmSet(effect.routeNo, effect.nodeName)
            }
        } else {
            showPermissionSheet = true
        }
    }

    fun checkAndStartWithNotificationPerm(effect: AlarmSettingSideEffect.AlarmConfirmed) {
        val hasNotificationPerm = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            )
        when {
            hasNotificationPerm -> {
                onAlarmSet(effect.routeNo, effect.nodeName)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.POST_NOTIFICATIONS,
            ) -> showPermissionSheet = true

            else -> {
                pendingAlarm = effect
                notificationPermLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val locationPermLauncher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        val alarm = pendingAlarm
        pendingAlarm = null
        if (granted) {
            alarm?.let { checkAndStartWithNotificationPerm(it) }
        } else {
            showLocationPermissionSheet = true
        }
    }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            AlarmSettingSideEffect.NavigateBack -> onBackClick()
            is AlarmSettingSideEffect.AlarmConfirmed -> {
                val hasLocationPerm = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
                when {
                    hasLocationPerm -> checkAndStartWithNotificationPerm(effect)
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ) -> showLocationPermissionSheet = true

                    else -> {
                        pendingAlarm = effect
                        locationPermLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }

    AlarmSettingScreen(
        state = state,
        onBackClick = { viewModel.onIntent(AlarmSettingIntent.ClickBack) },
        onAlarmClick = { node -> viewModel.onIntent(AlarmSettingIntent.SelectNode(node)) },
        onDismissBottomSheet = { viewModel.onIntent(AlarmSettingIntent.DismissBottomSheet) },
        onConfirmAlarm = { viewModel.onIntent(AlarmSettingIntent.ConfirmAlarm) },
        onSelectStopsBefore = { stops -> viewModel.onIntent(AlarmSettingIntent.SelectStopsBefore(stops)) },
    )

    if (showPermissionSheet) {
        NotificationPermissionBottomSheet(
            onConfirm = {
                showPermissionSheet = false
                openNotificationSettings(context)
            },
            onDismiss = { showPermissionSheet = false },
        )
    }

    if (showLocationPermissionSheet) {
        LocationPermissionBottomSheet(
            onConfirm = {
                showLocationPermissionSheet = false
                openAppSettings(context)
            },
            onDismiss = { showLocationPermissionSheet = false },
        )
    }
}

private fun openNotificationSettings(context: Context) {
    context.startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        },
    )
}

private fun openAppSettings(context: Context) {
    context.startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationPermissionBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = SRTheme.colors.background,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.space20)
                .padding(bottom = Spacing.space32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.space12),
        ) {
            Text(
                text = stringResource(R.string.location_permission_title),
                style = SRTheme.typography.bodyXMM,
                color = SRTheme.colors.textPrimary,
            )
            Text(
                text = stringResource(R.string.location_permission_message),
                style = SRTheme.typography.bodySR,
                color = SRTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Spacing.space8))
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SRTheme.colors.blue50),
            ) {
                Text(
                    text = stringResource(R.string.location_permission_go_to_settings),
                    style = SRTheme.typography.bodyMSB,
                    color = SRTheme.colors.white,
                    modifier = Modifier.padding(vertical = Spacing.space8),
                )
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SRTheme.colors.coolNeutral95),
                elevation = null,
            ) {
                Text(
                    text = stringResource(R.string.alarm_setting_cancel),
                    style = SRTheme.typography.bodyMSB,
                    color = SRTheme.colors.textSecondary,
                    modifier = Modifier.padding(vertical = Spacing.space8),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationPermissionBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = SRTheme.colors.background,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.space20)
                .padding(bottom = Spacing.space32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.space12),
        ) {
            Text(
                text = stringResource(R.string.notification_permission_title),
                style = SRTheme.typography.bodyXMM,
                color = SRTheme.colors.textPrimary,
            )
            Text(
                text = stringResource(R.string.notification_permission_message),
                style = SRTheme.typography.bodySR,
                color = SRTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Spacing.space8))
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SRTheme.colors.blue50),
            ) {
                Text(
                    text = stringResource(R.string.notification_permission_go_to_settings),
                    style = SRTheme.typography.bodyMSB,
                    color = SRTheme.colors.white,
                    modifier = Modifier.padding(vertical = Spacing.space8),
                )
            }
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SRTheme.colors.coolNeutral95),
                elevation = null,
            ) {
                Text(
                    text = stringResource(R.string.alarm_setting_cancel),
                    style = SRTheme.typography.bodyMSB,
                    color = SRTheme.colors.textSecondary,
                    modifier = Modifier.padding(vertical = Spacing.space8),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmSettingScreen(
    state: AlarmSettingState,
    onBackClick: () -> Unit,
    onAlarmClick: (BusNode) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onConfirmAlarm: () -> Unit,
    onSelectStopsBefore: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
            SRIconButton(
                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_arrow_left),
                contentDescription = stringResource(R.string.back),
                onClick = { onBackClick() },
                size = 28.dp,
            )
            Text(
                text = stringResource(R.string.alarm_setting),
                style = SRTheme.typography.bodyXMM,
                color = SRTheme.colors.textPrimary,
                modifier = Modifier.weight(1f),
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SRTheme.colors.blue50)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.space20, vertical = Spacing.space12),
                verticalArrangement = Arrangement.spacedBy(Spacing.space8),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.space4),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(DesignR.drawable.ic_bus),
                        contentDescription = null,
                        tint = SRTheme.colors.blue50,
                    )
                    Text(
                        text = state.routeNo,
                        style = SRTheme.typography.bodySR,
                        color = SRTheme.colors.blue50,
                    )
                }

                if (state.boardingNodeName.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.alarm_boarding_node_label, state.boardingNodeName),
                        style = SRTheme.typography.bodySR,
                        color = SRTheme.colors.textPrimary,
                    )
                }

                Text(
                    text = stringResource(R.string.alarm_setting_select_stop_hint),
                    style = SRTheme.typography.bodySR,
                    color = SRTheme.colors.textSecondary,
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(state.nodes, key = { _, node -> node.nodeId }) { index, node ->
                    Box {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = SRTheme.colors.coolNeutral95,
                        )
                        AlarmSettingStopItem(
                            node = node,
                            isFirst = index == 0,
                            isLast = index == state.nodes.lastIndex,
                            onAlarmClick = { onAlarmClick(node) },
                        )
                    }
                }
                item { Spacer(Modifier.height(Spacing.space64)) }
            }
        }
    }

    if (state.selectedNode != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissBottomSheet,
            sheetState = sheetState,
            containerColor = SRTheme.colors.background,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        ) {
            AlarmConfirmBottomSheet(
                routeNo = state.routeNo,
                nodeName = state.selectedNode.nodeName,
                selectedStopsBefore = state.selectedStopsBefore,
                onSelectStops = onSelectStopsBefore,
                onConfirm = onConfirmAlarm,
            )
        }
    }
}

@Composable
private fun AlarmSettingStopItem(
    node: BusNode,
    isFirst: Boolean,
    isLast: Boolean,
    onAlarmClick: () -> Unit,
) {
    val lineColor = SRTheme.colors.blue50
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                imageVector = ImageVector.vectorResource(DesignR.drawable.ic_chevron_down_circle),
                contentDescription = null,
                tint = SRTheme.colors.coolNeutral70,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = Spacing.space20)
                    .size(StopIconSize),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.space4),
        ) {
            Text(
                text = node.nodeName,
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.textPrimary,
            )
            node.nodeNo?.let {
                Text(
                    text = it,
                    style = SRTheme.typography.bodyXSR,
                    color = SRTheme.colors.textSecondary,
                )
            }
        }
        SRIconButton(
            imageVector = ImageVector.vectorResource(DesignR.drawable.ic_bell),
            contentDescription = null,
            tint = SRTheme.colors.blue50,
            size = 28.dp,
            onClick = onAlarmClick,
        )
        Spacer(Modifier.width(Spacing.space20))
    }
}

@Composable
private fun AlarmConfirmBottomSheet(
    routeNo: String,
    nodeName: String,
    selectedStopsBefore: Int,
    onSelectStops: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SRTheme.colors.background)
            .padding(horizontal = Spacing.space20)
            .padding(bottom = Spacing.space32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.space8),
    ) {
        Text(
            text = stringResource(R.string.alarm_setting_confirm_title),
            style = SRTheme.typography.bodyXMM,
            color = SRTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.alarm_setting_route_stop, routeNo, nodeName),
            style = SRTheme.typography.bodySR,
            color = SRTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.space8))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.space8),
        ) {
            for (stops in 1..3) {
                val selected = stops == selectedStopsBefore
                Button(
                    onClick = { onSelectStops(stops) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) SRTheme.colors.blue50 else SRTheme.colors.coolNeutral95,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.alarm_stops_before_format, stops),
                        style = SRTheme.typography.bodyXSR,
                        color = if (selected) SRTheme.colors.background else SRTheme.colors.textSecondary,
                    )
                }
            }
        }
        Spacer(Modifier.height(Spacing.space8))
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SRTheme.colors.blue50),
        ) {
            Text(
                text = stringResource(R.string.alarm_setting_button),
                style = SRTheme.typography.bodyMM,
                color = SRTheme.colors.background,
                modifier = Modifier.padding(vertical = Spacing.space8),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmSettingScreenPreview() {
    SRTheme {
        AlarmSettingScreen(
            state = AlarmSettingState(
                routeNo = "51",
                nodes = listOf(
                    BusNode("N1", "노포동", cityCode = CityCode.BUSAN, nodeNo = "12345"),
                    BusNode("N2", "부산대학교앞", cityCode = CityCode.BUSAN),
                    BusNode("N3", "온천장역", cityCode = CityCode.BUSAN),
                ),
            ),
            onBackClick = {},
            onAlarmClick = {},
            onDismissBottomSheet = {},
            onConfirmAlarm = {},
            onSelectStopsBefore = {},
        )
    }
}

@Preview(showBackground = true, name = "BottomSheet 표시")
@Composable
private fun AlarmConfirmBottomSheetPreview() {
    SRTheme {
        AlarmConfirmBottomSheet(
            routeNo = "51",
            nodeName = "부산대학교앞",
            selectedStopsBefore = 1,
            onSelectStops = {},
            onConfirm = {},
        )
    }
}
