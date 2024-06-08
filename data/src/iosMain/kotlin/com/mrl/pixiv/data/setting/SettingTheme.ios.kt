package com.mrl.pixiv.data.setting

import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle

actual fun getAppCompatDelegateThemeMode(): SettingTheme {
    return UIApplication.sharedApplication.keyWindow?.traitCollection?.userInterfaceStyle?.let {
        when (it) {
            UIUserInterfaceStyle.UIUserInterfaceStyleLight -> SettingTheme.LIGHT
            UIUserInterfaceStyle.UIUserInterfaceStyleDark -> SettingTheme.DARK
            else -> SettingTheme.SYSTEM
        }
    } ?: SettingTheme.SYSTEM
}

actual fun setAppCompatDelegateThemeMode(theme: SettingTheme) {
    UIApplication.sharedApplication.keyWindow?.let {
        it.overrideUserInterfaceStyle = when (theme) {
            SettingTheme.LIGHT -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
            SettingTheme.DARK -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            SettingTheme.SYSTEM -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
        }
    }
}