package com.example.livetracking.utils

import java.util.*

fun String.toTitleCase():String{
    return this.lowercase(Locale.getDefault())
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
}