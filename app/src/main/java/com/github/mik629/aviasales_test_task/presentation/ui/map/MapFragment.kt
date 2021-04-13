package com.github.mik629.aviasales_test_task.presentation.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.appComponent
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapFragment : Fragment(R.layout.map_screen) {
    @Inject
    lateinit var mapViewModelFactory: MapViewModelFactory.Factory

    private val viewModel: MapViewModel by viewModels(
        factoryProducer = {
            mapViewModelFactory.create(
                departureCityId = arguments?.getLong(ARG_DEPARTURE_CITY_ID) ?: -1,
                arrivalCityId = arguments?.getLong(ARG_ARRIVAL_CITY_ID) ?: -1,
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { map ->
            viewModel.destinations.observe(viewLifecycleOwner) { viewState ->
                when (viewState) {
                    is ViewState.Success -> addMarkers(
                        map = map,
                        departureCity = viewState.result.first,
                        arrivalCity = viewState.result.second
                    )
                }
            }
        }
    }

    private fun addMarkers(map: GoogleMap, departureCity: City, arrivalCity: City) {
        map.addMarker(buildMarker(city = departureCity))
        map.addMarker(buildMarker(city = arrivalCity))
    }

    private fun buildMarker(city: City): MarkerOptions =
        MarkerOptions()
            .position(
                LatLng(
                    city.location.lat,
                    city.location.lon
                )
            ).title(city.name)


    private fun animateMarker(map: GoogleMap) {

    }

    companion object {
        private const val ARG_DEPARTURE_CITY_ID = "departureCityId"
        private const val ARG_ARRIVAL_CITY_ID = "arrivalCityId"

        @JvmStatic
        fun newInstance(departureCityId: Long, arrivalCityId: Long): Fragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putLong(ARG_DEPARTURE_CITY_ID, departureCityId)
            args.putLong(ARG_ARRIVAL_CITY_ID, arrivalCityId)
            fragment.arguments = args
            return fragment
        }
    }
}