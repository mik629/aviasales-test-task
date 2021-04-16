package com.github.mik629.aviasales_test_task.presentation.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MapViewModel(
    private val destinationsRepository: DestinationsRepository
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
                destinationsRepository.restoreDepartureCity() to destinationsRepository.restoreArrivalCity()
            }.onSuccess { destinationPoints ->
                _destinations.value = ViewState.success(data = destinationPoints)
            }.onFailure { e ->
                Timber.e(e)
                _destinations.value = ViewState.error(error = e)
            }
        }
    }

    fun buildMarker(city: City): MarkerOptions =
        MarkerOptions()
            .position(
                LatLng(
                    city.location.lat,
                    city.location.lon
                )
            ).title(city.abbreviation ?: city.name)

    class Factory @Inject constructor(
        private val destinationsRepository: DestinationsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == MapViewModel::class.java)
            return MapViewModel(
                destinationsRepository = destinationsRepository
            ) as T
        }
    }
}
