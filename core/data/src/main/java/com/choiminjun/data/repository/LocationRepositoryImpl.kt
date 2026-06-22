package com.choiminjun.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.choiminjun.common.util.calculateDistance
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.location.Coordinate
import com.choiminjun.domain.model.location.NearestNodeResult
import com.choiminjun.domain.repository.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationRepository {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun observeLocation(): Flow<Coordinate> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL_MS)
            .setMinUpdateIntervalMillis(LOCATION_MIN_INTERVAL_MS)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    Timber.d("위치 수신: lat=${location.latitude}, lon=${location.longitude}, accuracy=${location.accuracy}m")
                    trySend(Coordinate(location.latitude, location.longitude))
                }
            }
        }

        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            fusedClient.removeLocationUpdates(callback)
        }
    }

    override fun observeNearestNode(
        nodes: List<BusNode>,
        boardingIndex: Int,
        destIndex: Int,
    ): Flow<NearestNodeResult> = flow {
        val windowNodes = nodes.subList(boardingIndex, destIndex + 1).toMutableList()
        var windowStart = boardingIndex

        Timber.d("노드 윈도우 초기화: boardingIndex=$boardingIndex, destIndex=$destIndex, windowSize=${windowNodes.size}")

        emitAll(
            observeLocation().mapNotNull { coord ->
                if (windowNodes.isEmpty()) return@mapNotNull null

                val nearestIdx = windowNodes.indices.minByOrNull { i ->
                    calculateDistance(
                        coord.latitude, coord.longitude,
                        windowNodes[i].latitude!!, windowNodes[i].longitude!!,
                    )
                } ?: return@mapNotNull null

                if (nearestIdx > 0) {
                    windowStart += nearestIdx
                    repeat(nearestIdx) { windowNodes.removeAt(0) }
                    Timber.d("윈도우 전진: nearestIdx=$nearestIdx, windowStart=$windowStart, remaining=${windowNodes.size - 1}")
                }

                val nearestNode = windowNodes[0]
                val remaining = windowNodes.size - 1
                val distance = calculateDistance(
                    coord.latitude, coord.longitude,
                    nearestNode.latitude!!, nearestNode.longitude!!,
                )

                Timber.d("최근접 정류장: [${nearestNode.nodeName}] remaining=$remaining, distance=${distance.toInt()}m")

                NearestNodeResult(
                    nearestNodeIndex = windowStart,
                    destIndex = destIndex,
                    nearestNodeName = nearestNode.nodeName,
                    distanceMeters = distance,
                    remaining = remaining,
                )
            },
        )
    }

    companion object {
        private const val LOCATION_INTERVAL_MS = 10_000L
        private const val LOCATION_MIN_INTERVAL_MS = 7_000L
    }
}
