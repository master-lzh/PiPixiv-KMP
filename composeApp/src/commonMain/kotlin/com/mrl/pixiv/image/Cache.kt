package com.mrl.pixiv.image

import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache

expect object CoilDiskCache {
    fun get(context: PlatformContext): DiskCache
}

object CoilMemoryCache {
    private var instance: MemoryCache? = null

    fun get(context: PlatformContext): MemoryCache {
        return instance ?: run {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.25)
                .build()
                .also { instance = it }
        }
    }
}