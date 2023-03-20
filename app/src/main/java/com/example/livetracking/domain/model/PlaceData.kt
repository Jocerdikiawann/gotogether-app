package com.example.livetracking.domain.model

import android.graphics.Bitmap
import com.google.android.libraries.places.api.model.Place

data class PlaceData(
    val place:Place,
    val photoBitmap:Bitmap?
)
