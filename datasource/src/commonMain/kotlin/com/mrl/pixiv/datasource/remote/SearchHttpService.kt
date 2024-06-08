package com.mrl.pixiv.datasource.remote

import com.mrl.pixiv.data.search.SearchAutoCompleteQuery
import com.mrl.pixiv.data.search.SearchAutoCompleteResp
import com.mrl.pixiv.data.search.SearchIllustQuery
import com.mrl.pixiv.data.search.SearchIllustResp
import com.mrl.pixiv.data.search.toMap
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class SearchHttpService(
    private val httpClient: HttpClient,
) {
    suspend fun searchIllust(searchIllustQuery: SearchIllustQuery) =
        httpClient.safeGet<SearchIllustResp>("/v1/search/illust") {
            searchIllustQuery.toMap().forEach { (key, value) ->
                parameter(key, value)
            }
        }

    suspend fun searchIllust(queryMap: Map<String, String>) =
        httpClient.safeGet<SearchIllustResp>("/v1/search/illust") {
            queryMap.forEach { (key, value) ->
                parameter(key, value)
            }
        }

    suspend fun searchAutoComplete(searchAutoCompleteQuery: SearchAutoCompleteQuery) =
        httpClient.safeGet<SearchAutoCompleteResp>("/v2/search/autocomplete") {
            searchAutoCompleteQuery.toMap().forEach { (key, value) ->
                parameter(key, value)
            }
        }
}