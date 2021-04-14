package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.databinding.DestionationItemBinding
import com.github.mik629.aviasales_test_task.domain.models.City

class CityArrayAdapter(context: Context, private val items: List<City>) :
    ArrayAdapter<City>(context, R.layout.destionation_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: DestionationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
        val item = getItem(position)
        view.findViewById<TextView>(R.id.destination).text = item?.fullName ?: item?.name
        return view
    }

    override fun getFilter(): Filter =
        CityFilter(cityAdapter = this, context = context, items = items)
}