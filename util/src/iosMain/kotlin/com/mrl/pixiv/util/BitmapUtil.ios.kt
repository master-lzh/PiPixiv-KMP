package com.mrl.pixiv.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import co.touchlab.kermit.Logger
import coil3.request.ImageResult
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import okio.Path
import org.jetbrains.skia.Image
import org.jetbrains.skia.impl.use
import platform.Foundation.NSArray
import platform.Foundation.NSData
import platform.Foundation.NSPredicate
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHAssetCollection
import platform.Photos.PHAssetCollectionChangeRequest
import platform.Photos.PHAssetCollectionSubtypeAny
import platform.Photos.PHAssetCollectionTypeAlbum
import platform.Photos.PHFetchOptions
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIImage

actual typealias Bitmap = org.jetbrains.skia.Bitmap

@OptIn(BetaInteropApi::class)
actual fun Bitmap.saveToAlbum(
    fileName: String,
    type: PictureType,
    callback: (Boolean) -> Unit
): Boolean {
    Image.makeFromBitmap(this).encodeToData()?.use { data ->
        val nsData = data.bytes.usePinned {
            memScoped {
                NSData.create(bytes = allocArrayOf(it.get()), length = data.size.toULong())
            }
        }

        // 创建UIImage
        val uiImage = UIImage(data = nsData)
        // 获取自定义相册
        val fetchOptions = PHFetchOptions().apply {
            predicate = NSPredicate.predicateWithFormat(
                "title = %@",
                NSString.create(string = DOWNLOAD_DIR)
            )
        }
        val fetchResult = PHAssetCollection.fetchAssetCollectionsWithType(
            PHAssetCollectionTypeAlbum,
            PHAssetCollectionSubtypeAny,
            fetchOptions
        )
        var assetCollection: PHAssetCollection? = if (fetchResult.count > 0u) {
            fetchResult.firstObject() as? PHAssetCollection
        } else {
            null
        }
        if (assetCollection == null) {
            PHPhotoLibrary.sharedPhotoLibrary().performChanges({
                assetCollection = if (fetchResult.count > 0u) {
                    fetchResult.firstObject() as? PHAssetCollection
                } else {
                    // 如果相册不存在，则创建相册
                    val createAlbumRequest =
                        PHAssetCollectionChangeRequest.creationRequestForAssetCollectionWithTitle(
                            DOWNLOAD_DIR
                        )
                    createAlbumRequest.placeholderForCreatedAssetCollection.let { placeholder ->
                        PHAssetCollection.fetchAssetCollectionsWithLocalIdentifiers(
                            listOf(placeholder.localIdentifier),
                            null
                        ).firstObject() as? PHAssetCollection
                    }
                }
            }) { success, _ ->
                if (success) {
                    this.saveToAlbum(fileName, type, callback)
                }
            }
            return true
        }

        PHPhotoLibrary.sharedPhotoLibrary().performChanges({
            // 创建图片请求
            val createAssetRequest = PHAssetChangeRequest.creationRequestForAssetFromImage(uiImage)
            // 将图片添加到相册
            Logger.i("添加图片到相册: $assetCollection")
            assetCollection?.let {
                val addAssetRequest =
                    PHAssetCollectionChangeRequest.changeRequestForAssetCollection(it)
                createAssetRequest.placeholderForCreatedAsset?.let { placeholder ->
                    addAssetRequest?.addAssets(NSArray.create(listOf(placeholder)))
                }
            }
        }) { success, _ ->
            callback(success)
        }
    }
    return true
}

actual fun Bitmap.asComposeImage(): ImageBitmap = this.asComposeImageBitmap()

actual fun Path.toBitmap(): Bitmap? {
    TODO("Not yet implemented")
}

actual fun parseImageResult(imageResult: ImageResult): Bitmap? = imageResult.image?.toBitmap()
