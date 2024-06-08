package com.mrl.pixiv.picture.util

import android.app.Activity
import android.content.Intent
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import coil3.imageLoader
import coil3.request.ImageRequest
import com.mrl.pixiv.data.Illust
import com.mrl.pixiv.util.AppUtil
import com.mrl.pixiv.util.DOWNLOAD_DIR
import com.mrl.pixiv.util.PictureType
import com.mrl.pixiv.util.saveToAlbum
import okio.Path.Companion.toPath


@Composable
actual fun rememberShareLauncher(onResult: () -> Unit): Any =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 处理分享结果
        if (result.resultCode == Activity.RESULT_OK) {
            onResult()
        } else {
            // 分享失败或取消
        }
    }

@Suppress("UNCHECKED_CAST")
actual fun share(text: String, shareLauncher: Any) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }.let {
        Intent.createChooser(it, "Share")
    }
    if (shareLauncher is ManagedActivityResultLauncher<*, *>) {
        shareLauncher as ManagedActivityResultLauncher<Intent, ActivityResult>
        shareLauncher.launch(shareIntent)
    }
}

@Suppress("UNCHECKED_CAST")
actual suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any
): Boolean {
    val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        .absolutePath.let {
            (it.toPath() / DOWNLOAD_DIR / "${illust.id}_${currLongClickPic.first}${PictureType.PNG.extension}").toFile()
        }
    if (!file.exists()) {
        val imageLoader = AppUtil.appContext.imageLoader
        val request = ImageRequest
            .Builder(AppUtil.appContext)
            .data(currLongClickPic.second)
            .build()
        val result = imageLoader.execute(request)
        result.image?.asDrawable(AppUtil.appContext.resources)
            ?.toBitmap()
            ?.saveToAlbum(file.nameWithoutExtension, PictureType.PNG)
            ?: return true
    }
    val uri = FileProvider.getUriForFile(
        AppUtil.appContext,
        "${AppUtil.appContext.packageName}.fileprovider",
        file
    )
    // 分享图片
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (shareLauncher is ManagedActivityResultLauncher<*, *>) {
        shareLauncher as ManagedActivityResultLauncher<Intent, ActivityResult>
        shareLauncher.launch(intent)
    }
    return false
}