package com.github.mik629.aviasales_test_task.data.network.model

import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.domain.models.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AutocompleteResponse(
    val cities: List<CityDto> = emptyList()
)

@JsonClass(generateAdapter = true)
class CityDto(
    val id: Long,
    val fullName: String,
    val location: LocationDto,
    @Json(name = "city")
    val name: String
)

fun CityDto.toCity(): City =
    City(id = id, fullName = fullName, location = location.toLocation(), name = name)

@JsonClass(generateAdapter = true)
class LocationDto(
    val lat: Double,
    val lon: Double
)

fun LocationDto.toLocation(): Location =
    Location(lat = lat, lon = lon)
