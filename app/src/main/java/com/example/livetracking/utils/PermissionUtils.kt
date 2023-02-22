package com.example.livetracking.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat

class PermissionUtils(private val context: Context) {
    companion object {
        @SuppressLint("InlinedApi")
        val listPermission = arrayOf(
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        const val PERMISSION_CODE_REQUEST = 321

    }

    @SuppressLint("InlinedApi")
    fun listPermission(): Array<String> {
        val list = listPermission.toMutableList()
        list.addAll(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        return list.toTypedArray()
    }

    /**
     * check if app already have permission
     * @return Boolean
     * **/
    fun hasPermission(): Boolean {
        val hasPermission = arrayListOf<Boolean>()
        listPermission.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (it == Manifest.permission.ACCESS_FINE_LOCATION ||
                    it == Manifest.permission.ACCESS_COARSE_LOCATION
                ) {
                    return@forEach
                }
            }
            hasPermission.add(
                ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            )
            Log.e(
                "permission",
                "$it -> ${
                    ActivityCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }"
            )
        }
        return !hasPermission.contains(false)
    }
}