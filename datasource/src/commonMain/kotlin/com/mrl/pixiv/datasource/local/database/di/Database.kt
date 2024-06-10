package com.mrl.pixiv.datasource.local.database.di

import com.mrl.pixiv.datasource.local.database.DownloadDatabase

expect fun provideDownloadDatabase(
    dbFileName: String = "download.db",
): DownloadDatabase