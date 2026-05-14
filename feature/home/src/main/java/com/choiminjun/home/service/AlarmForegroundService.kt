package com.choiminjun.home.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.choiminjun.domain.repository.AlarmRepository
import com.choiminjun.home.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmForegroundService : Service() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var observeJob: Job? = null

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
        } else {
            scope.launch {
                val alarm = alarmRepository.observeAlarm().first()
                if (alarm.routeId.isBlank()) {
                    stopSelf()
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification(alarm.routeNo, alarm.destNodeName))
                    observeAlarmState()
                }
            }
        }

        return START_STICKY
    }

    private fun observeAlarmState() {
        observeJob?.cancel()
        observeJob = scope.launch {
            alarmRepository.observeAlarm().collect { alarm ->
                if (alarm.routeId.isBlank()) stopSelf()
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
            .setContentTitle(getString(R.string.alarm_notification_title))
            .setContentText(getString(R.string.alarm_notification_text, routeNo, nodeName))
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.alarm_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "stop_alarm_channel"
        const val EXTRA_ROUTE_NO = "extra_route_no"
        const val EXTRA_NODE_NAME = "extra_node_name"
    }
}

internal fun startAlarmService(context: Context, routeNo: String, nodeName: String) {
    val intent = Intent(context, AlarmForegroundService::class.java).apply {
        putExtra(AlarmForegroundService.EXTRA_ROUTE_NO, routeNo)
        putExtra(AlarmForegroundService.EXTRA_NODE_NAME, nodeName)
    }
    ContextCompat.startForegroundService(context, intent)
}

internal fun stopAlarmService(context: Context) {
    context.stopService(Intent(context, AlarmForegroundService::class.java))
}
