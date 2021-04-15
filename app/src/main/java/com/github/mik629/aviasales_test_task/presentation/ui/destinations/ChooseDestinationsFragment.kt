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
        binding.searchButton.setOnClickListener {
            viewModel.onSearchClick()
        }
        viewModel.destinations.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    showLoading(isLoading = true)
                }
                is ViewState.Success -> {
                    showLoading(isLoading = false)
                    binding.errorDesc.isVisible = false
                    binding.homeTitle.text = getString(R.string.choose_destinations_greeting)
                    binding.departurePoint.setAdapter(
                        createAutoCompleteAdapter(cities = viewState.result)
                    )
                    binding.arrivalPoint.setAdapter(
                        createAutoCompleteAdapter(cities = viewState.result)
                    )
                }
                is ViewState.Error -> {
                    binding.progressbar.isVisible = false
                    binding.errorDesc.isVisible = true
                    binding.homeTitle.isVisible = true
                    binding.homeTitle.text = getString(R.string.error_title_no_internet)
                    binding.errorDesc.text = buildSpannedString {
                        append(getString(R.string.error_desc_no_internet))
                        append(" ")
                        color(ContextCompat.getColor(requireContext(), R.color.pink)) {
                            underline {
                                append(getString(R.string.retry))
                            }
                        }
                    }
                    binding.errorDesc.setOnClickListener {
                        viewModel.refresh()
                    }
                }
            }
        }
        // todo make dropdown appear strictly below input field
        binding.departurePoint.setOnItemClickListener { parent, _, position, _ ->
            viewModel.saveDepartureChoice(parent.getItemAtPosition(position) as City)
        }
        binding.arrivalPoint.setOnItemClickListener { parent, _, position, _ ->
            viewModel.saveArrivalChoice(parent.getItemAtPosition(position) as City)
        }
        binding.arrivalPoint.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                viewModel.onSearchClick()
                true
            } else false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.isVisible = isLoading
        binding.homeTitle.isVisible = !isLoading
        binding.departureLayout.isVisible = !isLoading
        binding.arrivalLayout.isVisible = !isLoading
        binding.searchButton.isVisible = !isLoading
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
