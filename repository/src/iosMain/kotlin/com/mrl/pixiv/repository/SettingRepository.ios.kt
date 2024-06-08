package com.mrl.pixiv.repository

import com.mrl.pixiv.data.setting.SettingTheme
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.OSVersion
import org.jetbrains.skiko.available

actual fun defaultSettingTheme(): SettingTheme =
    if (available(OS.Ios to OSVersion(13))) SettingTheme.SYSTEM else SettingTheme.LIGHT