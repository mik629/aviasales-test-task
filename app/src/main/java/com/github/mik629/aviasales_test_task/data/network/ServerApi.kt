package com.github.mik629.aviasales_test_task.data.network

import com.github.mik629.aviasales_test_task.data.network.model.AutocompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {
    @GET("autocomplete")
    suspend fun autocomplete(
        @Query("term") airportAbbreviation: String = "mow",
        @Query("lang") languageCode: String = "en"
    ): AutocompleteResponse
}