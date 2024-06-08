package com.mrl.pixiv.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val <reified T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    inline get() = this.current ?: error("No ${T::class.simpleName} provided")

val LocalNavigator = staticCompositionLocalOf<NavHostController?> {
    null
}

val LocalSystemTheme = staticCompositionLocalOf<Boolean> {
    error("No SystemTheme provided")
}


//val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> {
//    null
//}
//
//val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope?> {
//    null
//}
//
//val LocalSharedKeyPrefix = compositionLocalOf {
//    ""
//}