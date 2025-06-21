package com.example.listo.shopping.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    fun create(): GeoCodingAPI
    {
        val retrofit= Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GeoCodingAPI::class.java)
    }
}