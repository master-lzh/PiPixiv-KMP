package com.mrl.pixiv.data.illust

import com.mrl.pixiv.data.GenerateMap
import com.mrl.pixiv.data.Restrict

@GenerateMap
data class IllustRecommendedQuery(
    val filter: String,
    val includeRankingIllusts: Boolean,
    val includePrivacyPolicy: Boolean,
)

@GenerateMap
data class IllustBookmarkAddReq(
    val illustId: Long,
    @Restrict
    val restrict: String = Restrict.PUBLIC,
    val tags: List<String>? = null,
)

@GenerateMap
data class IllustBookmarkDeleteReq(
    val illustId: Long,
)

@GenerateMap
data class IllustRelatedQuery(
    val illustId: Long,
    val filter: String,
)

@GenerateMap
data class IllustDetailQuery(
    val filter: String,
    val illustId: Long,
)

@GenerateMap
data class IllustBookmarkDetailQuery(
    val illustId: Long,
)
