package com.mrl.pixiv.picture.util

import androidx.compose.runtime.Composable
import coil3.PlatformContext
import com.mrl.pixiv.data.Illust
import com.mrl.pixiv.repository.IllustRepository


@Composable
expect fun rememberShareLauncher(onResult: () -> Unit): Any

expect fun share(text: String, shareLauncher: Any)


expect suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any,
    illustRepository: IllustRepository,
    context: PlatformContext,
): Boolean