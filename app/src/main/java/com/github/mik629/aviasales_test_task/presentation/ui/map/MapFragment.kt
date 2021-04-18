package com.github.mik629.aviasales_test_task.presentation.ui.map

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.os.Bundle
import android.util.Property
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.appComponent
import com.github.mik629.aviasales_test_task.databinding.MapScreenBinding
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.github.mik629.aviasales_test_task.presentation.ui.utils.dpToPx
import com.github.mik629.aviasales_test_task.presentation.ui.utils.showSnackBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.maps.android.SphericalUtil
import javax.inject.Inject


class MapFragment : Fragment(R.layout.map_screen) {
    private val binding: MapScreenBinding by viewBinding()

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
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { map ->
            viewModel.destinations.observe(viewLifecycleOwner) { viewState ->
                when (viewState) {
                    is ViewState.Loading -> showLoading(isLoading = true)
                    is ViewState.Success -> {
                        showLoading(isLoading = false)
                        addMarkers(
                            map = map,
                            departureCity = viewState.result.first,
                            arrivalCity = viewState.result.second
                        )
                    }
                    is ViewState.Error -> {
                        showLoading(isLoading = false)
                        binding.root.showSnackBar(getString(R.string.error_map_unexpected))
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.map.isVisible = !isLoading
        binding.progress.progressbar.isVisible = isLoading
    }

    private fun addMarkers(map: GoogleMap, departureCity: City, arrivalCity: City) {
        val departurePoint = LatLng(departureCity.location.lat, departureCity.location.lon)
        val arrivalPoint = LatLng(arrivalCity.location.lat, arrivalCity.location.lon)
        // todo centralize map in the middle of destinations (mb use liteMode)
        // todo rotate jet icon into arrival direction
        map.addMarker(viewModel.buildMarker(city = departureCity)).showInfoWindow()
        map.addMarker(viewModel.buildMarker(city = arrivalCity)).showInfoWindow()

        map.addPolyline(
            PolylineOptions()
                .add(departurePoint, arrivalPoint)
                .width(requireContext().resources.getDimension(R.dimen.jet_trajectory_width))
                .color(ContextCompat.getColor(requireContext(), R.color.light_blue))
                .geodesic(true)
        )
        addAnimation(arrivalCity, departureCity, map, departurePoint, arrivalPoint)

        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                LatLngBounds.Builder()
                    .include(departurePoint)
                    .include(arrivalPoint)
                    .build(),
                dpToPx(
                    dp = resources.getDimension(R.dimen.map_marker_padding_to_border),
                    density = resources.displayMetrics.density
                )
            )
        )
    }

    private fun addAnimation(
        arrivalCity: City,
        departureCity: City,
        map: GoogleMap,
        departurePoint: LatLng,
        arrivalPoint: LatLng
    ) {
        if (
            viewModel.areLocationsWithinArea(
                pointA = arrivalCity.location,
                pointB = departureCity.location,
                latThreshold = resources.getInteger(R.integer.map_max_lat_diff_to_fit),
                lngThreshold = resources.getInteger(R.integer.map_max_lng_diff_to_fit)
            )
        ) {
            animateMarker(
                marker = map.addMarker(
                    MarkerOptions()
                        .position(departurePoint)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                ),
                finalPosition = arrivalPoint
            )
        } else {
            binding.root.showSnackBar(
                message = getString(R.string.error_destinations_far_away),
                duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            )
        }
    }

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
