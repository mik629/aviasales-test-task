package com.github.mik629.aviasales_test_task.data.network.model

import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.domain.models.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CityDto(
    val id: Long,
    val fullName: String?,
    val location: LocationDto,
    @Json(name = "city")
    val name: String,
    @Json(name = "iata")
    val abbreviations: List<String> = emptyList()
)

fun CityDto.toCity(): City =
    City(
        id = id,
        fullName = fullName,
        location = location.toLocation(),
        name = name,
        abbreviation = abbreviations.firstOrNull()
    )

@JsonClass(generateAdapter = true)
class LocationDto(
    val lat: Double,
    val lon: Double
)

fun LocationDto.toLocation(): Location =
    Location(lat = lat, lon = lon)