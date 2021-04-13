package com.github.mik629.aviasales_test_task.presentation.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel @AssistedInject constructor(
    private val destinationsRepository: DestinationsRepository,
    @Assisted(value = DEPARTURE_CITY_ID) private val departureCityId: Long,
    @Assisted(value = ARRIVAL_CITY_ID) private val arrivalCityId: Long
) : ViewModel() {

    private val _destinations: MutableLiveData<ViewState<Pair<City, City>, Throwable>> =
        MutableLiveData()
    val destinations: LiveData<ViewState<Pair<City, City>, Throwable>>
        get() =
            _destinations

    init {
        viewModelScope.launch {
            _destinations.value = ViewState.loading()
            runCatching {
                destinationsRepository.getCity(id = departureCityId) to destinationsRepository.getCity(
                    id = arrivalCityId
                )
            }.onSuccess { destinationPoints ->
                _destinations.value = ViewState.success(data = destinationPoints)
            }.onFailure { e ->
                Timber.e(e)
                _destinations.value = ViewState.error(error = e)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(value = DEPARTURE_CITY_ID) departureCityId: Long,
            @Assisted(value = ARRIVAL_CITY_ID) arrivalCityId: Long
        ): MapViewModel
    }

    companion object {
        const val DEPARTURE_CITY_ID = "departureCityId"
        const val ARRIVAL_CITY_ID = "arrivalCityId"
    }
}

class MapViewModelFactory @AssistedInject constructor(
    private val viewModelFactory: MapViewModel.Factory,
    @Assisted(value = MapViewModel.DEPARTURE_CITY_ID) private val departureCityId: Long,
    @Assisted(value = MapViewModel.ARRIVAL_CITY_ID) private val arrivalCityId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass == MapViewModel::class.java)
        return viewModelFactory.create(
            departureCityId = departureCityId,
            arrivalCityId = arrivalCityId
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(value = MapViewModel.DEPARTURE_CITY_ID) departureCityId: Long,
            @Assisted(value = MapViewModel.ARRIVAL_CITY_ID) arrivalCityId: Long
        ): MapViewModelFactory
    }
}