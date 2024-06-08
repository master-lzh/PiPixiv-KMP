package com.mrl.pixiv.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.mrl.pixiv.common.router.Destination
import com.mrl.pixiv.common.router.DestinationsDeepLink

actual typealias Intent = android.content.Intent

@Composable
actual fun HandleDeeplink(
    intent: Intent?,
    navHostController: NavHostController
) {
    LaunchedEffect(intent) {
        if (intent != null) {
            val data = intent.data ?: return@LaunchedEffect
            when {
                DestinationsDeepLink.illustRegex.matches(data.toString()) -> {
                    navHostController.navigate("${Destination.PictureDeeplinkScreen.route}/${data.lastPathSegment}")
                }

                DestinationsDeepLink.userRegex.matches(data.toString()) -> {
                    navHostController.navigate("${Destination.OtherProfileDetailScreen.route}/${data.lastPathSegment}")
                }
            }
        }
    }
}