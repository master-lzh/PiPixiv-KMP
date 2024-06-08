package com.mrl.pixiv.datasource.remote

import com.mrl.pixiv.data.ugoira.UgoiraMetadataResp
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class UgoiraHttpService(
    private val httpClient: HttpClient
) {
    suspend fun getUgoiraMetadata(illustId: Long) =
        httpClient.safeGet<UgoiraMetadataResp>("/v1/ugoira/metadata") {
            parameter("illust_id", illustId)
        }
}