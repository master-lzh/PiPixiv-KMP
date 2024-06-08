package com.mrl.pixiv.profile

import android.os.Build
import com.mrl.pixiv.data.setting.SettingTheme
import org.jetbrains.compose.resources.StringResource
import pipixiv.feature.profile.generated.resources.Res
import pipixiv.feature.profile.generated.resources.theme_dark
import pipixiv.feature.profile.generated.resources.theme_light
import pipixiv.feature.profile.generated.resources.theme_system

actual val options: Map<SettingTheme, StringResource> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mapOf(
            SettingTheme.SYSTEM to Res.string.theme_system,
            SettingTheme.LIGHT to Res.string.theme_light,
            SettingTheme.DARK to Res.string.theme_dark,
        )
    } else {
        mapOf(
            SettingTheme.LIGHT to Res.string.theme_light,
            SettingTheme.DARK to Res.string.theme_dark,
        )
    }