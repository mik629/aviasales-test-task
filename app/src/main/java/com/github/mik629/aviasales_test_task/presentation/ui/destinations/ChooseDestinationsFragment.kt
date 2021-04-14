package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
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
                is ViewState.Success -> {
                    binding.departurePoint.setAdapter(
                        createAutoCompleteAdapter(cities = viewState.result)
                    )
                    binding.arrivalPoint.setAdapter(
                        createAutoCompleteAdapter(cities = viewState.result)
                    )
                }
            }
        }
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
