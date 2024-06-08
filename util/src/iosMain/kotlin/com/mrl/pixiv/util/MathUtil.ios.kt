package com.mrl.pixiv.util

import kotlin.math.round

actual fun Float.format(digits: Int): String {
    val factor = 100
    val roundedValue = round(this * factor) / factor
    return roundedValue.toString()
}