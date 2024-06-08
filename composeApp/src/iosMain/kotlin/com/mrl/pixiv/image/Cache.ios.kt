package com.mrl.pixiv.image

import coil3.PlatformContext
import coil3.disk.DiskCache
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual object CoilDiskCache {
    private const val FOLDER_NAME = "image_cache"
    private var instance: DiskCache? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun get(context: PlatformContext): DiskCache {
        return instance ?: run {
            val safeCacheDir = NSFileManager.defaultManager.URLForDirectory(
                directory = NSCachesDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )?.path
            // Create the singleton disk cache instance.
            DiskCache.Builder()
                .directory(requireNotNull(safeCacheDir).toPath() / FOLDER_NAME)
                .build()
                .also { instance = it }
        }
    }
}