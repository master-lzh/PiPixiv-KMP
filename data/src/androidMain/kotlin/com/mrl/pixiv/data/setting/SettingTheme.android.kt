package com.mrl.pixiv.data.setting

import androidx.appcompat.app.AppCompatDelegate

actual fun getAppCompatDelegateThemeMode(): SettingTheme =
    when (AppCompatDelegate.getDefaultNightMode()) {
        AppCompatDelegate.MODE_NIGHT_NO -> SettingTheme.LIGHT
        AppCompatDelegate.MODE_NIGHT_YES -> SettingTheme.DARK
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> SettingTheme.SYSTEM
        else -> SettingTheme.SYSTEM
    }

actual fun setAppCompatDelegateThemeMode(theme: SettingTheme) {
    AppCompatDelegate.setDefaultNightMode(
        when (theme) {
            SettingTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            SettingTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            SettingTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}