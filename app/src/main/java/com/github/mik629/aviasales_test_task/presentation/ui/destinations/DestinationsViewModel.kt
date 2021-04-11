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
    private val destinations: LiveData<ViewState<List<City>, Throwable>>
        get() =
            _destinations

    init {
        viewModelScope.launch {
            _destinations.value = ViewState.loading()
            runCatching {
                destinationsRepository.getCities()
            }.onSuccess { cities ->
                ViewState.success(data = cities)
            }.onFailure { e ->
                Timber.e(e)
                ViewState.error(error = e)
            }
        }
    }

    fun onSearchClick() {
        router.navigateTo(Screens.mapFragment())
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
