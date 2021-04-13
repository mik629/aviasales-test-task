package com.github.mik629.aviasales_test_task.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Long,
    val fullName: String?,
    val location: Location,
    val name: String
) : Parcelable

@Parcelize
data class Location(
    val lat: Double,
    val lon: Double
) : Parcelable