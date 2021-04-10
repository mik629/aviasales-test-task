package com.github.mik629.aviasales_test_task.data.network

import com.github.mik629.aviasales_test_task.data.network.model.AutocompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {
    @GET("autocomplete")
    suspend fun getMovie(
        @Query("term") airportAbbreviation: String,
        @Query("lang") languageCode: String
    ): AutocompleteResponse
}