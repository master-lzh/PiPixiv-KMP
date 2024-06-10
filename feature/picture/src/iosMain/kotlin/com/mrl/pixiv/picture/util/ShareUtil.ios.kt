package com.mrl.pixiv.picture.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import com.mrl.pixiv.common.coroutine.launchIO
import com.mrl.pixiv.common.coroutine.launchUI
import com.mrl.pixiv.data.Illust
import com.mrl.pixiv.datasource.local.database.DownloadEntity
import com.mrl.pixiv.repository.IllustRepository
import com.mrl.pixiv.util.PictureType
import com.mrl.pixiv.util.currentTimeMillis
import com.mrl.pixiv.util.saveToAlbum
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Photos.PHAsset
import platform.Photos.PHImageContentModeDefault
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.UIKit.UIActivityTypeMail
import platform.UIKit.UIActivityTypePrint
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIColor.Companion.clearColor
import platform.UIKit.UIPopoverArrowDirectionAny
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController

@Composable
actual fun rememberShareLauncher(onResult: () -> Unit): Any {
    return LocalUIViewController.current to onResult
}

@Suppress("UNCHECKED_CAST")
actual fun share(text: String, shareLauncher: Any) {
    if (shareLauncher is Pair<*, *>) {
        val (launcher, onResult) = shareLauncher as Pair<UIViewController, () -> Unit>
        val activityItems = listOf(text)
        val activityController = UIActivityViewController(activityItems, null)
        activityController.setExcludedActivityTypes(
            listOf(
                UIActivityTypeMail,
                UIActivityTypePrint
            )
        )
        val popoverController = activityController.popoverPresentationController()
        with(launcher) {
            view.backgroundColor = clearColor
            popoverController?.sourceView = view
            view.bounds.useContents {
                popoverController?.sourceRect = CGRectMake(
                    this.size.width / 2,
                    this.size.height,
                    0.0,
                    0.0
                )
            }
            popoverController?.permittedArrowDirections = UIPopoverArrowDirectionAny
        }
        activityController.setCompletionHandler { _, completed ->
            if (completed) {
                onResult()
            }
        }
        launcher.presentViewController(activityController, true, null)
    }
}


@Suppress("UNCHECKED_CAST")
actual suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any,
    illustRepository: IllustRepository,
    context: PlatformContext
): Boolean {
    if (shareLauncher is Pair<*, *>) {
        val (launcher, onResult) = shareLauncher as Pair<UIViewController, () -> Unit>
        // 查询图片是否已下载
        val downloadEntity = illustRepository.queryDownload(illust.id, currLongClickPic.first)
        if (downloadEntity != null) {
            shareImageByIdentifier(downloadEntity.path, launcher, onResult)
        } else {
            val imageLoader = SingletonImageLoader.get(context)
            val request = ImageRequest
                .Builder(context)
                .data(currLongClickPic.second)
                .build()
            val result = imageLoader.execute(request)
            result.image?.toBitmap()
                ?.saveToAlbum("", PictureType.PNG) { success, identifier ->
                    if (success) {
                        launchIO {
                            illustRepository.insertDownload(
                                DownloadEntity(
                                    illustId = illust.id,
                                    picIndex = currLongClickPic.first,
                                    title = "",
                                    url = currLongClickPic.second,
                                    path = identifier,
                                    createTime = currentTimeMillis(),
                                )
                            )
                        }
                        shareImageByIdentifier(identifier, launcher, onResult)
                    }
                }
        }
        return true
    } else {
        return false
    }
}

private fun shareImageByIdentifier(
    identifier: String,
    shareLauncher: UIViewController,
    onResult: () -> Unit
) {
    val asset =
        PHAsset.fetchAssetsWithLocalIdentifiers(listOf(identifier), null).firstObject() as? PHAsset
    asset?.let {
        val options = PHImageRequestOptions().apply {
            synchronous = true
        }
        PHImageManager.defaultManager().requestImageForAsset(
            asset,
            CGSizeMake(asset.pixelWidth.toDouble(), asset.pixelHeight.toDouble()),
            PHImageContentModeDefault,
            options
        ) { image, _ ->
            image?.let {
                val activityItems = listOf(it)
                val activityController = UIActivityViewController(activityItems, null)
                activityController.setCompletionHandler { _, completed ->
                    if (completed) {
                        onResult()
                    }
                }
                val popoverController = activityController.popoverPresentationController()
                with(shareLauncher) {
                    view.backgroundColor = platform.UIKit.UIColor.clearColor
                    popoverController?.sourceView = view
                    view.bounds.useContents {
                        popoverController?.sourceRect = CGRectMake(
                            this.size.width / 2,
                            this.size.height,
                            0.0,
                            0.0
                        )
                    }
                    popoverController?.permittedArrowDirections = UIPopoverArrowDirectionAny
                    launchUI {
                        presentViewController(activityController, true, null)
                    }
                }
            }
        }
    }
}