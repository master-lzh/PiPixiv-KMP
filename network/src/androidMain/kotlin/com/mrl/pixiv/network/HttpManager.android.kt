package com.mrl.pixiv.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual val baseHttpClient: HttpClient
    get() = httpClient(OkHttp) {
        config {
            retryOnConnectionFailure(true)
        }
    }

actual val baseImageHttpClient: HttpClient
    get() = imageHttpClient(OkHttp) {
        config {
            retryOnConnectionFailure(true)
        }
    }