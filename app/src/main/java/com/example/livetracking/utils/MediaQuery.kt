package com.example.livetracking.utils

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt

fun Dp.from(ctx: Context, defaultW: Double = 375.0, defaultH: Double = 812.0): Dp {
    val density = ctx.resources.displayMetrics.density
    val currentW = ctx.resources.displayMetrics.widthPixels.dp / density
    val currentH = ctx.resources.displayMetrics.heightPixels.dp / density
    val currentDiagonalScreen =
        sqrt(currentW.value.toDouble().pow(2) + currentH.value.toDouble().pow(2)).dp

    val defW = defaultW.pow(2)
    val defH = defaultH.pow(2)
    val defDiagonalScreen = sqrt(defW + defH)

    val resultCompare = currentDiagonalScreen.value / defDiagonalScreen
    val result = (this.value * resultCompare).toInt()

    return result.dp
}

fun TextUnit.from(
    ctx: Context,
    defaultScreenWidth: Double = 375.0,
    defaultScreenHeight: Double = 812.0
): TextUnit {
    val density = ctx.resources.displayMetrics.density
    val currentW = ctx.resources.displayMetrics.heightPixels.dp / density
    val currentH = ctx.resources.displayMetrics.widthPixels.dp / density
    val currentDiagonalScreen =
        sqrt(currentW.value.toDouble().pow(2) + currentH.value.toDouble().pow(2)).dp


    //now design using default size with w = 375 h = 812
    val defWidth = defaultScreenWidth.pow(2)
    val defHeight = defaultScreenHeight.pow(2)
    val defScreenDiagonal = sqrt(defWidth + defHeight)

    val resultCompare = currentDiagonalScreen.value / defScreenDiagonal
    val result = (this.value * resultCompare).toInt()

    return result.sp

}