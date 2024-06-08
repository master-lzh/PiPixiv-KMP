package com.mrl.pixiv.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import co.touchlab.kermit.Logger as Log

object NetworkExceptionUtil : CoroutineScope by MainScope() {
    private const val EVENT_HTTP_EXCEPTION = "http_exception"

    fun resolveException(e: Throwable) {
        Log.e(this::class.simpleName!!, e)
    }
}