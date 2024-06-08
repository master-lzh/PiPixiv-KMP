package com.mrl.pixiv.datasource.remote

import com.mrl.pixiv.data.auth.AuthTokenFieldReq
import com.mrl.pixiv.data.auth.AuthTokenResp
import com.mrl.pixiv.data.auth.toMap
import io.ktor.client.HttpClient
import io.ktor.http.parameters

class UserAuthHttpService(
    private val httpClient: HttpClient
) {
    suspend fun login(
        authTokenFieldReq: AuthTokenFieldReq
    ) = httpClient.safePostForm<AuthTokenResp>(
        urlString = "/auth/token",
        formParameters = parameters {
            authTokenFieldReq.toMap().map {
                append(it.key, it.value.toString())
            }
        }
    ) {
        headers.remove("Authorization")
    }
}