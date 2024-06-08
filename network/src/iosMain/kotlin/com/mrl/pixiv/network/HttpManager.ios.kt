package com.mrl.pixiv.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual val baseHttpClient: HttpClient
    get() = httpClient(Darwin) {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }

actual val baseImageHttpClient: HttpClient
    get() = imageHttpClient(Darwin) {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }