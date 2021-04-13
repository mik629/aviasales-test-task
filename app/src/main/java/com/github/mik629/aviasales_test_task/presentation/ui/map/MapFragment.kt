package com.github.mik629.aviasales_test_task.presentation.ui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.domain.models.City
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(R.layout.map_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { map ->
            addMarker(map = map, argKey = ARG_DEPARTURE_POINT)
            addMarker(map = map, argKey = ARG_ARRIVAL_POINT)
        }
    }

    private fun addMarker(map: GoogleMap, argKey: String) {
        arguments?.getParcelable<City>(argKey)
            ?.let { city ->
                map.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                city.location.lat,
                                city.location.lon
                            )
                        ).title(city.name)
                )
            }
    }

    companion object {
        private const val ARG_DEPARTURE_POINT = "departurePoint"
        private const val ARG_ARRIVAL_POINT = "arrivalPoint"

        @JvmStatic
        fun newInstance(departurePoint: City, arrivalPoint: City): Fragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putParcelable(ARG_DEPARTURE_POINT, departurePoint)
            args.putParcelable(ARG_ARRIVAL_POINT, arrivalPoint)
            fragment.arguments = args
            return fragment
        }
    }
}