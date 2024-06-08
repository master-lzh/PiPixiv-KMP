package com.mrl.pixiv.util

import androidx.compose.ui.graphics.ImageBitmap
import coil3.request.ImageResult
import io.ktor.client.HttpClient
import io.ktor.client.request.head
import io.ktor.http.HttpStatusCode
import okio.Path


// 下载文件夹为DCIM/PiPixiv
const val DOWNLOAD_DIR = "PiPixiv"

enum class PictureType(val extension: String) {
    PNG(".png"),
    JPG(".jpg"),
    JPEG(".jpeg");

    fun parseType(extension: String): PictureType {
        return when (extension) {
            ".png" -> PNG
            ".jpg" -> JPG
            ".jpeg" -> JPEG
            else -> PNG
        }
    }
}

expect class Bitmap

expect fun Bitmap.saveToAlbum(
    fileName: String,
    type: PictureType,
    callback: (Boolean) -> Unit = {}
): Boolean

expect fun Bitmap.asComposeImage(): ImageBitmap

suspend fun calculateImageSize(url: String): Float {
    return try {
        val httpClient = HttpClient()
        val resp = httpClient.head(url) {
            headers["Referer"] = "https://www.pixiv.net/"
        }

        val responseCode = resp.status
        if (responseCode == HttpStatusCode.OK) {
            val contentLength = resp.headers["Content-Length"]
            if (contentLength != null) {
                val sizeInBytes = contentLength.toLong()
                return sizeInBytes / 1024f / 1024f
            }
        }
        return 0f
    } catch (e: Exception) {
        e.printStackTrace()
        0f
    }
}

expect fun Path.toBitmap(): Bitmap?

expect fun parseImageResult(imageResult: ImageResult): Bitmap?