package com.example.livetracking.data.local

import android.content.SharedPreferences
import com.google.gson.Gson

class Persistence(
    private val sharedPreferences: SharedPreferences,
    val gson: Gson = Gson()
) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val key_token = "key_token"
    fun setToken(token: String) {
        editor.putString(key_token, token)
        editor.apply()
    }

    fun getToken(): String? = sharedPreferences.getString(key_token, null)

    private fun dropToken() {
        editor.remove(key_token)
        editor.apply()
    }

    private val key_place_id = "key_place_id"
    fun setPlaceId(id: String) {
        editor.putString(key_place_id, id)
        editor.apply()
    }

    fun getPlaceId(): String? = sharedPreferences.getString(key_place_id, null)

    private fun dropPlaceId() {
        editor.remove(key_place_id)
        editor.apply()
    }

    fun dropAll() {
        dropToken()
        dropPlaceId()
    }
}