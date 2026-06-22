package com.choiminjun.alarm.alarmring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.alarm.R
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.designsystem.R as DesignR

@Composable
internal fun AlarmRingRoute(
    onDismiss: () -> Unit,
    viewModel: AlarmRingViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            AlarmRingSideEffect.NavigateToHome -> onDismiss()
        }
    }

    AlarmRingScreen(
        state = state,
        onConfirmClick = { viewModel.onIntent(AlarmRingIntent.ConfirmDismiss) },
    )
}

@Composable
private fun AlarmRingScreen(
    state: AlarmRingState,
    onConfirmClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SRTheme.colors.blue60)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = Spacing.space20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(DesignR.drawable.ic_bell),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(72.dp),
        )
        Spacer(Modifier.height(Spacing.space32))
        Text(
            text = stringResource(R.string.alarm_ring_title),
            style = SRTheme.typography.headingXLSB,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.space24))
        Text(
            text = stringResource(R.string.alarm_setting_route_stop, state.alarmInfo.routeNo, state.alarmInfo.destNodeName),
            style = SRTheme.typography.bodyXMM,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Spacing.space8))
        Text(
            text = stringResource(R.string.alarm_stops_before_format, state.alarmInfo.stopsBeforeAlarm),
            style = SRTheme.typography.bodyMM,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Spacing.space48))
        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        ) {
            Text(
                text = stringResource(R.string.alarm_ring_confirm),
                style = SRTheme.typography.bodyMSB,
                color = SRTheme.colors.blue50,
                modifier = Modifier.padding(vertical = Spacing.space8),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmRingScreenPreview() {
    SRTheme {
        AlarmRingScreen(
            state = AlarmRingState(
                AlarmInfo(
                    routeId = "R001",
                    routeNo = "51",
                    destNodeId = "N001",
                    destNodeName = "부산대학교앞",
                ),
            ),
            onConfirmClick = {},
        )
    }
}
