package com.mrl.pixiv.util

actual fun Float.format(digits: Int): String = "%.${digits}f".format(this)