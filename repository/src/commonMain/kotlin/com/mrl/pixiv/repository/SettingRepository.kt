package com.mrl.pixiv.repository

import com.mrl.pixiv.data.setting.SettingTheme
import com.mrl.pixiv.datasource.local.SettingDataSource
import kotlinx.coroutines.flow.map

class SettingRepository(
    private val settingDataSource: SettingDataSource
) {
    val allSettings = settingDataSource.data
    val allSettingsSync = settingDataSource.syncData

    val settingTheme = settingDataSource.data.map {
        enumValueOf<SettingTheme>(
            it.theme.ifEmpty {
                defaultSettingTheme().name
            }
        )
    }

    fun setSettingTheme(theme: SettingTheme) = settingDataSource.updateData {
        it.copy(theme = theme.name)
    }

    val enableBypassSniffing = settingDataSource.data.map { it.enableBypassSniffing }
    fun setEnableBypassSniffing(enable: Boolean) = settingDataSource.updateData {
        it.copy(enableBypassSniffing = enable)
    }

    val pictureSourceHost = settingDataSource.data.map { it.imageHost }
    fun setPictureSourceHost(host: String) = settingDataSource.updateData {
        it.copy(imageHost = host)
    }

    val hasShowBookmarkTip = settingDataSource.data.map { it.hasShowBookmarkTip }
    fun setHasShowBookmarkTip(hasShow: Boolean) = settingDataSource.updateData {
        it.copy(hasShowBookmarkTip = hasShow)
    }
}

expect fun defaultSettingTheme(): SettingTheme