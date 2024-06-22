package com.mrl.pixiv.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortrait
import platform.UIKit.UIInterfaceOrientationPortraitUpsideDown
import platform.UIKit.UIWindow

@Composable
actual fun getOrientation(): Orientation =
    (UIApplication.sharedApplication.windows.first() as? UIWindow)?.windowScene?.interfaceOrientation?.let {
        when (it) {
            UIInterfaceOrientationLandscapeLeft, UIInterfaceOrientationLandscapeRight -> Orientation.Horizontal
            UIInterfaceOrientationPortrait, UIInterfaceOrientationPortraitUpsideDown -> Orientation.Vertical
            else -> Orientation.Vertical
        }
    } ?: Orientation.Vertical

@Composable
actual fun getScreenWidth(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp()
}

@Composable
actual fun getScreenHeight(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp()
}