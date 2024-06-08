package com.mrl.pixiv.picture.util

import androidx.compose.runtime.Composable
import com.mrl.pixiv.data.Illust


@Composable
expect fun rememberShareLauncher(onResult: () -> Unit): Any

expect fun share(text: String, shareLauncher: Any)


expect suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any
): Boolean