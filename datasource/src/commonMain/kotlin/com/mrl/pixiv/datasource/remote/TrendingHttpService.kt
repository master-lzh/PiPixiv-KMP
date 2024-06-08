package com.mrl.pixiv.datasource.remote

import com.mrl.pixiv.data.Filter
import com.mrl.pixiv.data.search.TrendingTagsResp
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class TrendingHttpService(
    private val httpClient: HttpClient
) {
    suspend fun trendingTags(filter: Filter) =
        httpClient.safeGet<TrendingTagsResp>("/v1/trending-tags/illust") {
            parameter("filter", filter.name)
        }
}