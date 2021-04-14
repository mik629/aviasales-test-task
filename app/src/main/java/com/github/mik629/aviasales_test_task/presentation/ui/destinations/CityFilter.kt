package com.github.mik629.aviasales_test_task.presentation.ui.destinations

import android.content.Context
import android.widget.Filter
import com.github.mik629.aviasales_test_task.domain.models.City

class CityFilter(
    private val cityAdapter: CityArrayAdapter,
    private val context: Context,
    private val items: List<City>
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        return if (!constraint.isNullOrEmpty()) {
            val results = items.filter { item ->
                item.name
                    .toLowerCase(context.resources.configuration.locales[0])
                    .startsWith(constraint)
            }
            FilterResults().apply {
                values = results
                count = results.size
            }
        } else {
            FilterResults()
        }
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        if (results != null && results.count > 0) {
            val filteredList: ArrayList<City> = results.values as ArrayList<City>
            with(cityAdapter) {
                clear()
                addAll(filteredList)
                notifyDataSetChanged()
            }
        }
    }
}