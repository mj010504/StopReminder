package com.choiminjun.stopreminder.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.repository.AlarmRepository
import com.choiminjun.domain.usecase.ObserveNearestNodeUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import com.choiminjun.alarm.R as AlarmR

@AndroidEntryPoint
class AlarmForegroundService : Service() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var observeNearestNode: ObserveNearestNodeUseCase

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var observeJob: Job? = null
    private var locationJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val routeNo = intent?.getStringExtra(EXTRA_ROUTE_NO)
        val nodeName = intent?.getStringExtra(EXTRA_NODE_NAME)

        if (routeNo != null && nodeName != null) {
            startForeground(NOTIFICATION_ID, buildNotification(routeNo, nodeName))
            observeAlarmState()
            scope.launch {
                val alarm = alarmRepository.observeAlarm().first()
                if (alarm.routeId.isNotBlank()) startLocationTracking(alarm)
            }
        } else {
            scope.launch {
                val alarm = alarmRepository.observeAlarm().first()
                if (alarm.routeId.isBlank()) {
                    stopSelf()
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification(alarm.routeNo, alarm.destNodeName))
                    observeAlarmState()
                    startLocationTracking(alarm)
                }
            }
        }

        return START_STICKY
    }

    private fun observeAlarmState() {
        observeJob?.cancel()
        observeJob = scope.launch {
            alarmRepository.observeAlarm().collect { alarm ->
                if (alarm.routeId.isBlank()) {
                    stopSelf()
                }
            }
        }
    }

    private fun startLocationTracking(alarmInfo: AlarmInfo) {
        locationJob?.cancel()
        locationJob = scope.launch {
            while (isActive) {
                try {
                    observeNearestNode(alarmInfo.routeId, alarmInfo.boardingNodeId, alarmInfo.destNodeId)
                        .collect { result ->
                            alarmRepository.updateNearestNode(result)
                            if (result.remaining in 0..alarmInfo.stopsBeforeAlarm) {
                                alarmRepository.triggerAlarm()
                                stopSelf()
                            }
                        }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Timber.e(e, "위치 추적 오류, 5초 후 재시도")
                    delay(5.seconds)
                }
            }
        }
    }

    private fun buildNotification(routeNo: String, nodeName: String): Notification {
        val tapIntent = packageManager.getLaunchIntentForPackage(packageName)
            ?.apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP }
            ?: Intent()

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(AlarmR.string.alarm_notification_title))
            .setContentText(getString(AlarmR.string.alarm_notification_text, routeNo, nodeName))
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(AlarmR.string.alarm_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationJob?.cancel()
        scope.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "stop_alarm_channel"
        const val EXTRA_ROUTE_NO = "extra_route_no"
        const val EXTRA_NODE_NAME = "extra_node_name"
    }
}

fun startAlarmService(context: Context, routeNo: String, nodeName: String) {
    val intent = Intent(context, AlarmForegroundService::class.java).apply {
        putExtra(AlarmForegroundService.EXTRA_ROUTE_NO, routeNo)
        putExtra(AlarmForegroundService.EXTRA_NODE_NAME, nodeName)
    }
    ContextCompat.startForegroundService(context, intent)
}

fun stopAlarmService(context: Context) {
    context.stopService(Intent(context, AlarmForegroundService::class.java))
}
