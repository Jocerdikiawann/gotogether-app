package com.example.livetracking.domain.model.response

import com.example.livetracking.domain.utils.ErrorStatus

data class GeocodingResponse(
    val error_message: String? = null,
    val plus_code: PlusCode? = null,
    val results: List<Result>,
    val status: ErrorStatus
)

data class PlusCode(
    val compound_code: String,
    val global_code: String
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val plus_code: PlusCode? = null,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Bounds,
    val bounds: Bounds? = null
)

data class Bounds(
    val northeast: Location,
    val southwest: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)