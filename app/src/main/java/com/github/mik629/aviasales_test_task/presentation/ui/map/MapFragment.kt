package com.github.mik629.aviasales_test_task.presentation.ui.map

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.os.Bundle
import android.util.Property
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.appComponent
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import javax.inject.Inject


class MapFragment : SupportMapFragment() {
    @Inject
    lateinit var mapViewModelFactory: MapViewModel.Factory

    private val viewModel: MapViewModel by viewModels(
        factoryProducer = { mapViewModelFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync { map ->
            viewModel.destinations.observe(viewLifecycleOwner) { viewState ->
                when (viewState) {
                    // todo handle errors
                    is ViewState.Success -> with(viewState.result) {
                        addMarkers(
                            map = map,
                            departureCity = first,
                            arrivalCity = second
                        )
                    }
                }
            }
        }
    }

    private fun addMarkers(map: GoogleMap, departureCity: City, arrivalCity: City) {
        val departurePoint = LatLng(departureCity.location.lat, departureCity.location.lon)
        val arrivalPoint = LatLng(arrivalCity.location.lat, arrivalCity.location.lon)
        // todo centralize map in the middle of destinations (mb use liteMode)
        // todo rotate jet icon into arrival direction
        map.addMarker(buildMarker(city = departureCity)).showInfoWindow()
        map.addMarker(buildMarker(city = arrivalCity)).showInfoWindow()

        map.addPolyline(
            PolylineOptions()
                .add(departurePoint, arrivalPoint)
                .width(requireContext().resources.getDimension(R.dimen.jet_trajectory_width))
                .color(ContextCompat.getColor(requireContext(), R.color.light_blue))
                .geodesic(true)
        )
        animateMarker(
            marker = map.addMarker(
                MarkerOptions()
                    .position(departurePoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
            ),
            finalPosition = arrivalPoint
        )

        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                LatLngBounds.Builder()
                    .include(departurePoint)
                    .include(arrivalPoint)
                    .build(),
                80
            )
        )
    }

    private fun buildMarker(city: City): MarkerOptions =
        MarkerOptions()
            .position(
                LatLng(
                    city.location.lat,
                    city.location.lon
                )
            ).title(city.abbreviation ?: city.name)


    private fun animateMarker(marker: Marker, finalPosition: LatLng) {
        val typeEvaluator = TypeEvaluator<LatLng> { fraction, startValue, endValue ->
            SphericalUtil.interpolate(startValue, endValue, fraction.toDouble())
        }
        val property: Property<Marker, LatLng> = Property.of(
            Marker::class.java,
            LatLng::class.java,
            MARKER_POSITION_PROPERTY
        )
        val animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition)
        animator.duration = JET_ANIMATION_DURATION
        animator.startDelay = JET_ANIMATION_START_DELAY
        animator.start()
    }

    companion object {
        private const val JET_ANIMATION_DURATION: Long = 3000
        private const val JET_ANIMATION_START_DELAY: Long = 100
        private const val MARKER_POSITION_PROPERTY = "position"

        @JvmStatic
        fun newInstance(): Fragment =
            MapFragment()
    }
}
