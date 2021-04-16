package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.underline
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.appComponent
import com.github.mik629.aviasales_test_task.databinding.ChooseDestionationsScreenBinding
import com.github.mik629.aviasales_test_task.domain.models.City
import com.github.mik629.aviasales_test_task.presentation.ui.ViewState
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import javax.inject.Inject

class ChooseDestinationsFragment : Fragment(R.layout.choose_destionations_screen) {
    private val binding: ChooseDestionationsScreenBinding by viewBinding()

    @Inject
    lateinit var destinationsViewModelFactory: DestinationsViewModel.Factory

    private val viewModel: DestinationsViewModel by viewModels(
        factoryProducer = { destinationsViewModelFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener { safeSearchClick() }
        binding.departurePoint.setOnItemClickListener { parent, _, position, _ ->
            viewModel.saveDepartureChoice(parent.getItemAtPosition(position) as City)
        }
        binding.arrivalPoint.setOnItemClickListener { parent, _, position, _ ->
            viewModel.saveArrivalChoice(parent.getItemAtPosition(position) as City)
        }
        binding.arrivalPoint.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                safeSearchClick()
                true
            } else false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.destinations.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> showLoading(isLoading = true)
                is ViewState.Success -> showDestinationsLayout(viewState)
                is ViewState.Error -> showErrorLayout()
            }
        }
        setSavedChoice(
            city = viewModel.departureChoice,
            autocompleteTextView = binding.departurePoint
        )
        setSavedChoice(
            city = viewModel.arrivalChoice,
            autocompleteTextView = binding.arrivalPoint
        )
    }

    private fun setSavedChoice(city: City?, autocompleteTextView: MaterialAutoCompleteTextView) {
        if (city != null) {
            autocompleteTextView.setText(city.name)
        }
    }

    private fun showDestinationsLayout(viewState: ViewState.Success<List<City>>) {
        showLoading(isLoading = false)
        binding.homeTitle.text = getString(R.string.choose_destinations_greeting)
        binding.departurePoint.setAdapter(
            createAutoCompleteAdapter(cities = viewState.result)
        )
        binding.arrivalPoint.setAdapter(
            createAutoCompleteAdapter(cities = viewState.result)
        )
    }

    private fun showErrorLayout() {
        showDestinationsLayout(isVisible = false)
        binding.progressbar.isVisible = false
        binding.homeTitle.isVisible = true
        binding.homeTitle.text = getString(R.string.error_general_title)
        binding.errorDesc.text = buildSpannedString {
            append(getString(R.string.error_general_desc))
            append(" ")
            color(ContextCompat.getColor(requireContext(), R.color.pink)) {
                underline {
                    append(getString(R.string.retry))
                }
            }
            append(" ")
            append(getString(R.string.error_talk_to_support))
        }
        binding.errorDesc.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun safeSearchClick() {
        val isDepartureInvalid = setErrorOnInvalidCity(
            autocompleteTextView = binding.departurePoint,
            city = viewModel.departureChoice
        )
        val isArrivalInvalid = setErrorOnInvalidCity(
            autocompleteTextView = binding.arrivalPoint,
            city = viewModel.arrivalChoice
        )
        if (!isDepartureInvalid && !isArrivalInvalid) {
            viewModel.onSearchClick()
        }
    }

    private fun setErrorOnInvalidCity(
        autocompleteTextView: MaterialAutoCompleteTextView,
        city: City?
    ): Boolean =
        when {
            autocompleteTextView.text?.toString().isNullOrBlank() -> {
                autocompleteTextView.error = getString(R.string.error_departure_required)
                true
            }
            city == null -> {
                autocompleteTextView.error = getString(R.string.error_unknown_city)
                true
            }
            else -> false
        }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.isVisible = isLoading
        showDestinationsLayout(isVisible = !isLoading)
    }

    private fun showDestinationsLayout(isVisible: Boolean) {
        binding.homeTitle.isVisible = isVisible
        binding.departureLayout.isVisible = isVisible
        binding.arrivalLayout.isVisible = isVisible
        binding.searchButton.isVisible = isVisible
        binding.errorDesc.isVisible = !isVisible
    }

    private fun createAutoCompleteAdapter(cities: List<City>) =
        CityArrayAdapter(
            context = requireContext(),
            items = ArrayList(cities)
        )

    companion object {
        @JvmStatic
        fun newInstance(): Fragment =
            ChooseDestinationsFragment()
    }
}
