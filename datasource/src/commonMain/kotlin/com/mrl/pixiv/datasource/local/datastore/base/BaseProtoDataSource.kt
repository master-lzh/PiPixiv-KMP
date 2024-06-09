package com.mrl.pixiv.datasource.local.datastore.base

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import co.touchlab.kermit.Logger
import com.mrl.pixiv.common.coroutine.launchIO
import com.mrl.pixiv.common.data.DispatcherEnum
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

abstract class BaseProtoDataSource<T>(
    private val dataStore: DataStore<T>
) : KoinComponent {
    private val ioDispatcher: CoroutineDispatcher by inject(named(DispatcherEnum.IO))

    val data = dataStore.data.catch {
        if (it is IOException) {
            Logger.e("IOException: ", it, this@BaseProtoDataSource::class.simpleName!!)
            emit(defaultValue())
        } else {
            throw it
        }
    }

    val syncData: T
        get() = runBlocking { data.first() }

    fun updateData(update: (T) -> T) {
        launchIO {
            dataStore.updateData {
                update(it)
            }
        }
    }

    abstract fun defaultValue(): T
}

