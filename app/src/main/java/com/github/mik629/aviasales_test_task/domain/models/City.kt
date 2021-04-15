package com.github.mik629.aviasales_test_task.domain.models

data class City(
    val id: Long,
    val fullName: String?,
    val location: Location,
    val name: String,
    val abbreviation: String? = null
) {
    override fun toString(): String {
        return name
    }
}

data class Location(
    val lat: Double,
    val lon: Double
)