package com.mrl.pixiv.picture.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import com.mrl.pixiv.data.Illust
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
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


actual suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any
): Boolean {
    TODO("Not yet implemented")
}