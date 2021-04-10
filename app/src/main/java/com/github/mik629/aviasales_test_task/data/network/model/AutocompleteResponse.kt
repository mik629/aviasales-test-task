package com.github.mik629.aviasales_test_task.data.network.model

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
    val name: String
)

@JsonClass(generateAdapter = true)
class LocationDto(
    val lat: Double,
    val lon: Double
)
