package com.example.shopping_list.shopping.model


data class LocationData(
    val latitude:Double,
    val longitude:Double
)

data class GeocodingResponse(
    val results: List<AddressResult>,
    val status: String
)

data class AddressResult(
    val formatted_address:String
)

