package com.github.mik629.aviasales_test_task.data.repositories

import com.github.mik629.aviasales_test_task.data.network.ServerApi
import com.github.mik629.aviasales_test_task.data.network.model.CityDto
import com.github.mik629.aviasales_test_task.data.network.model.toCity
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import com.github.mik629.aviasales_test_task.domain.models.City
import javax.inject.Inject

class DestinationsRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi
) : DestinationsRepository {
    // todo mb add sparsearray cache and inject city ids into VM

    override suspend fun getCities(): List<City> =
        serverApi.autocomplete()
            .cities
            .map(CityDto::toCity)
}