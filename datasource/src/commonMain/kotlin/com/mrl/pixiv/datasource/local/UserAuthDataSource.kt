package com.mrl.pixiv.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mrl.pixiv.datasource.local.base.BaseDataSource
import com.mrl.pixiv.datasource.local.base.dataStorePath

class UserAuthDataSource(
    userAuthDataStore: DataStore<Preferences>,
) : BaseDataSource(userAuthDataStore) {
    companion object {
        val KEY_USER_REFRESH_TOKEN = stringPreferencesKey("user_refresh_token")
        val KEY_USER_ACCESS_TOKEN = stringPreferencesKey("user_access_token")
        val KEY_ACCESS_TOKEN_EXPIRES_TIME = longPreferencesKey("access_token_expires_time")
    }

    val userRefreshToken = createFiled(KEY_USER_REFRESH_TOKEN)

    val userAccessToken = createFiled(KEY_USER_ACCESS_TOKEN)

    val accessTokenExpiresTime = createFiled(KEY_ACCESS_TOKEN_EXPIRES_TIME)
}

val userAuthDataStore = PreferenceDataStoreFactory.createWithPath {
    dataStorePath("user_auth.preferences_pb")
}
