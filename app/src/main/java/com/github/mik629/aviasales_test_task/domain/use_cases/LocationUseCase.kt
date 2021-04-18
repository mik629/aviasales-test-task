package com.github.mik629.aviasales_test_task.domain.use_cases

import com.github.mik629.aviasales_test_task.domain.models.Location
import kotlin.math.abs

class LocationUseCase {
    fun areLocationsWithinArea(
        pointA: Location,
        pointB: Location,
        latThreshold: Int,
        lngThreshold: Int
    ): Boolean {
        val latDiff = abs(pointA.lat - pointB.lat)
        val longDiff = abs(pointA.lon - pointB.lon)
        return latDiff <= latThreshold && longDiff <= lngThreshold
    }
}
