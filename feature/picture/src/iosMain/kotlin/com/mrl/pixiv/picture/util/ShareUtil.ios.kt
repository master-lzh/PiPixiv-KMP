package com.mrl.pixiv.picture.util

import androidx.compose.runtime.Composable
import com.mrl.pixiv.data.Illust

@Composable
actual fun rememberShareLauncher(onResult: () -> Unit): Any {
    return Unit
}

actual fun share(text: String, shareLauncher: Any) {
}


actual suspend fun createShareImage(
    currLongClickPic: Pair<Int, String>,
    illust: Illust,
    shareLauncher: Any
): Boolean {
    TODO("Not yet implemented")
}