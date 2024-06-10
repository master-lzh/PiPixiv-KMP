package com.mrl.pixiv.datasource.local.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mrl.pixiv.datasource.local.database.DownloadDatabase
import com.mrl.pixiv.util.AppUtil
import kotlinx.coroutines.Dispatchers

actual fun provideDownloadDatabase(dbFileName: String): DownloadDatabase {
    val context = AppUtil.appContext
    val dbFile = context.getDatabasePath(dbFileName)
    return Room.databaseBuilder<DownloadDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}