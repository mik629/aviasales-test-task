package com.github.mik629.aviasales_test_task.data.repositories

import com.github.mik629.aviasales_test_task.data.network.ServerApi
import com.github.mik629.aviasales_test_task.data.network.model.toCity
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import com.github.mik629.aviasales_test_task.domain.models.City
import javax.inject.Inject

class DestinationsRepositoryImpl @Inject constructor(
    private val serverApi: ServerApi
) : DestinationsRepository {

    private var departureCity: City? = null
    private var arrivalCity: City? = null

    override suspend fun getCities(): List<City> =
        serverApi.autocomplete()
            .cities
            .map { cityDto ->
                val city = cityDto.toCity()
                city
            }

    override suspend fun storeDepartureCity(city: City) {
        departureCity = city
    }

    override suspend fun restoreDepartureCity(): City =
        requireNotNull(departureCity)

    override suspend fun storeArrivalCity(city: City) {
        arrivalCity = city
    }

    override suspend fun restoreArrivalCity(): City =
        requireNotNull(arrivalCity)

}