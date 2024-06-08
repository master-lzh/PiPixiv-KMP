package com.mrl.pixiv.repository

import android.os.Build
import com.mrl.pixiv.data.setting.SettingTheme

actual fun defaultSettingTheme(): SettingTheme =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) SettingTheme.SYSTEM else SettingTheme.LIGHT
