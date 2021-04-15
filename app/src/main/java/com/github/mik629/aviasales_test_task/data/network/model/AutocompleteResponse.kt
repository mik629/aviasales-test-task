package com.github.mik629.aviasales_test_task.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AutocompleteResponse(
    val cities: List<CityDto> = emptyList()
)
