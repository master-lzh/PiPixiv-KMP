package com.mrl.pixiv.util

import android.graphics.BitmapFactory.decodeFile
import android.os.Environment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil3.request.ImageResult
import okio.Path
import okio.Path.Companion.toPath
import java.io.FileOutputStream

actual typealias Bitmap = android.graphics.Bitmap

actual fun Bitmap.saveToAlbum(
    fileName: String,
    type: PictureType,
    callback: (Boolean) -> Unit
): Boolean {
    val compressFormat = when (type) {
        PictureType.PNG -> android.graphics.Bitmap.CompressFormat.PNG
        PictureType.JPEG -> android.graphics.Bitmap.CompressFormat.JPEG
        PictureType.JPG -> android.graphics.Bitmap.CompressFormat.JPEG
    }
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        .absolutePath
        .let { path ->
            val dir = path.toPath() / DOWNLOAD_DIR
            dir.toFile().mkdirs()
            val filePath = dir / "$fileName${type.extension}"
            FileOutputStream(filePath.toFile()).use { out ->
                if (compress(compressFormat, 100, out)) {
                    return true
                }
            }
        }
    return false
}

actual fun Bitmap.asComposeImage(): ImageBitmap = this.asImageBitmap()

actual fun Path.toBitmap(): Bitmap? {
    return try {
        decodeFile(this.toFile().absolutePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

actual fun parseImageResult(imageResult: ImageResult): Bitmap? =
    imageResult.image?.asDrawable(AppUtil.appContext.resources)?.toBitmap()

