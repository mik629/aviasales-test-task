package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.appComponent
import com.github.mik629.aviasales_test_task.databinding.ChooseDestionationsScreenBinding
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
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment =
            ChooseDestinationsFragment()
    }
}