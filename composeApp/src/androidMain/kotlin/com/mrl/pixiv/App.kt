package com.mrl.pixiv

import AppInitializer
import android.app.Application
import android.content.Context
import com.mrl.pixiv.util.AppUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App : Application() {
    companion object {
        lateinit var instance: App
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppUtil.init(this)
        AppInitializer.onApplicationStart {
            androidLogger()
            androidContext(this@App)
        }
//        GlobalScope.launch {
//            setAppCompatDelegateThemeMode(getAppThemeUseCase().first())
//        }
    }
}


//val Context.userAuthDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_auth")