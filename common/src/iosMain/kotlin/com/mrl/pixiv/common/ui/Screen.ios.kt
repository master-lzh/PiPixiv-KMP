package com.mrl.pixiv.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation

@Composable
actual fun getOrientation(): Orientation = when (UIDevice.currentDevice.orientation) {
    UIDeviceOrientation.UIDeviceOrientationLandscapeLeft, UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> Orientation.Horizontal
    UIDeviceOrientation.UIDeviceOrientationPortrait, UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> Orientation.Vertical
    else -> Orientation.Vertical
}

@Composable
actual fun getScreenWidth(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp()
}

@Composable
actual fun getScreenHeight(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp()
}