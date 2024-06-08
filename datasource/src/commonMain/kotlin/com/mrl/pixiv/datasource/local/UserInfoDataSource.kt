package com.mrl.pixiv.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import com.mrl.pixiv.data.user.UserInfo
import com.mrl.pixiv.data.user.UserInfoSerializer
import com.mrl.pixiv.datasource.local.base.BaseProtoDataSource
import com.mrl.pixiv.datasource.local.base.dataStorePath
import okio.FileSystem
import okio.SYSTEM

class UserInfoDataSource(
    userInfoDataSource: DataStore<UserInfo>,
) : BaseProtoDataSource<UserInfo>(userInfoDataSource) {
    override fun defaultValue(): UserInfo = UserInfo.defaultInstance
}

val userInfoDataStore = DataStoreFactory.create(
    storage = OkioStorage(
        fileSystem = FileSystem.SYSTEM,
        serializer = UserInfoSerializer,
        producePath = {
            dataStorePath("user_info.json")
        },
    ),
)