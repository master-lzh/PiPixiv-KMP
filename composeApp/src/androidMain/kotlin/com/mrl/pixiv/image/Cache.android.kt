package com.mrl.pixiv.image

import coil3.PlatformContext
import coil3.disk.DiskCache
import okio.Path.Companion.toOkioPath

actual object CoilDiskCache {
    private const val FOLDER_NAME = "image_cache"
    private var instance: DiskCache? = null

    @Synchronized
    actual fun get(context: PlatformContext): DiskCache {
        return instance ?: run {
            val safeCacheDir = context.cacheDir.apply { mkdirs() }
            // Create the singleton disk cache instance.
            DiskCache.Builder()
                .directory(safeCacheDir.resolve(FOLDER_NAME).toOkioPath())
                .build()
                .also { instance = it }
        }
    }
}