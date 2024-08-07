package com.mrl.pixiv.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.ktor.http.Url
import io.ktor.http.decodeURLQueryComponent
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap

val Number.second: Long
    get() = this.toLong() * 1000

val Number.minute: Long
    get() = this.second * 60

val Number.hour: Long
    get() = this.minute * 60

val Number.day: Long
    get() = this.hour * 24

val Number.week: Long
    get() = this.day * 7

val Number.month: Long
    get() = this.day * 30

val String.queryParams: ImmutableMap<String, String>
    get() {
        val queryMap = mutableMapOf<String, String>()
        return try {
            val query = Url(this).encodedQuery
            for (param in query.split("&")) {
                val keyValuePair = param.split("=")
                queryMap[keyValuePair[0].decodeURLQueryComponent()] =
                    if (keyValuePair.size > 1) keyValuePair[1].decodeURLQueryComponent() else ""
            }
            queryMap.toImmutableMap()
        } catch (e: Exception) {
            persistentMapOf()
        }
    }

val Any.TAG: String
    get() = this::class.simpleName!!

val LazyStaggeredGridState.isScrollToTop: Boolean
    get() = firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0

val LazyStaggeredGridState.isScrollToBottom: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@Composable
fun LazyStaggeredGridState.OnScrollToBottom(loadingItemCount: Int, block: () -> Unit) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - loadingItemCount
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            block()
        }
    }
}

@Composable
fun LazyGridState.OnScrollToBottom(loadingItemCount: Int, block: () -> Unit) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - loadingItemCount
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            block()
        }
    }
}

@Composable
fun LazyListState.OnScrollToBottom(loadingItemCount: Int, block: () -> Unit) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - loadingItemCount
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            block()
        }
    }
}