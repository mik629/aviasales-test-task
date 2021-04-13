package com.github.mik629.aviasales_test_task

import com.github.mik629.aviasales_test_task.presentation.ui.destinations.ChooseDestinationsFragment
import com.github.mik629.aviasales_test_task.presentation.ui.map.MapFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun destinationsFragment(): FragmentScreen =
        FragmentScreen { ChooseDestinationsFragment.newInstance() }

    fun mapFragment(departureCityId: Long, arrivalCityId: Long): FragmentScreen =
        FragmentScreen {
            MapFragment.newInstance(
                departureCityId = departureCityId,
                arrivalCityId = arrivalCityId
            )
        }
}
