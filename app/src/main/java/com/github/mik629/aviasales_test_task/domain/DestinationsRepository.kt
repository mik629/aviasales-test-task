package com.github.mik629.aviasales_test_task.domain

import com.github.mik629.aviasales_test_task.domain.models.City

interface DestinationsRepository {
    suspend fun getCities(): List<City>

    suspend fun getCity(id: Long): City
}