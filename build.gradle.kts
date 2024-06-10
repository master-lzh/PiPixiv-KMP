plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    id(libs.plugins.androidApplication.get().pluginId) apply false
    id(libs.plugins.androidLibrary.get().pluginId) apply false
    id(libs.plugins.jetbrainsCompose.get().pluginId) apply false
    alias(kotlinx.plugins.compose.compiler) apply false
    id(kotlinx.plugins.multiplatform.get().pluginId) apply false
    alias(kotlinx.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}