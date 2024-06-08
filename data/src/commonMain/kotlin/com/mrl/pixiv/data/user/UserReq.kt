package com.mrl.pixiv.data.user

import com.mrl.pixiv.data.Filter
import com.mrl.pixiv.data.GenerateMap
import com.mrl.pixiv.data.Restrict

@GenerateMap
data class UserDetailQuery(
    val filter: String = Filter.ANDROID.value,
    val userId: Long,
)

@GenerateMap
data class UserIllustsQuery(
    val filter: String = Filter.ANDROID.value,
    val userId: Long,
    val type: String,
)

@GenerateMap
data class UserBookmarksIllustQuery(
    @Restrict
    val restrict: String,
    val userId: Long,
    val tag: String = "",
)

@GenerateMap
data class UserBookmarksNovelQuery(
    @Restrict
    val restrict: String,
    val userId: Long,
    val tag: String = "",
)

@GenerateMap
data class UserFollowAddReq(
    val userId: Long,
    val restrict: String,
)

@GenerateMap
data class UserFollowDeleteReq(
    val userId: Long,
)

@GenerateMap
data class UserBookmarkTagsQuery(
    val userId: Long,
    @Restrict
    val restrict: String,
)