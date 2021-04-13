package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.mik629.aviasales_test_task.Screens
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DestinationsViewModel(
    private val destinationsRepository: DestinationsRepository,
    private val router: Router
) : ViewModel() {
    private val _destinations: MutableLiveData<ViewState<List<City>, Throwable>> = MutableLiveData()
    val destinations: LiveData<ViewState<List<City>, Throwable>>
        get() =
            _destinations

    private var departurePoint: City? = null
    private var arrivalPoint: City? = null
    private var destinationsCache: List<City> = emptyList()

    init {
        viewModelScope.launch {
            _destinations.value = ViewState.loading()
            runCatching {
                destinationsRepository.getCities()
            }.onSuccess { cities ->
                _destinations.value = ViewState.success(data = cities)
                destinationsCache = cities
            }.onFailure { e ->
                Timber.e(e)
                _destinations.value = ViewState.error(error = e)
            }
        }
    }

    fun onSearchClick() {
        router.navigateTo(
            Screens.mapFragment(departurePoint = departurePoint!!, arrivalPoint = arrivalPoint!!)
        )
    }

    fun saveDepartureChoice(position: Int) {
        departurePoint = destinationsCache[position]
    }

    fun saveArrivalChoice(position: Int) {
        arrivalPoint = destinationsCache[position]
    }

    class Factory @Inject constructor(
        private val destinationsRepository: DestinationsRepository,
        private val router: Router
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            require(modelClass == DestinationsViewModel::class.java)
            return DestinationsViewModel(
                destinationsRepository = destinationsRepository,
                router = router
            ) as T
        }
    }
}
