package com.github.mik629.aviasales_test_task.domain.models

class City(
    val id: Long,
    val fullName: String?,
    val location: Location,
    val name: String
)

class Location(
    val lat: Double,
    val lon: Double
)