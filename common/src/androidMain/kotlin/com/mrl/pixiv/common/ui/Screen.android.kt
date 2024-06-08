package com.mrl.pixiv.common.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
actual fun getOrientation(): Orientation = when (LocalConfiguration.current.orientation) {
    Configuration.ORIENTATION_PORTRAIT -> Orientation.Vertical
    Configuration.ORIENTATION_LANDSCAPE -> Orientation.Horizontal
    else -> Orientation.Vertical
}

@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

@Composable
actual fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp