package com.example.listo.shopping.data

import com.example.listo.shopping.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingAPI
{
    //creating the url from where i can get the data
    @GET("maps/api/geocode/json")
    suspend fun getResponse(
        @Query("latlng") latlng:String,
        @Query("key") apiKey:String
    ): GeocodingResponse
}