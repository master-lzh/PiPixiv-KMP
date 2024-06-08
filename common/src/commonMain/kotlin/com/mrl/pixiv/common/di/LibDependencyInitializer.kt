package com.mrl.pixiv.common.di

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

object LibDependencyInitializer {

    var koinApp: KoinApplication? = null
        private set

    fun initialize(platformDeclaration: KoinAppDeclaration, modules: List<Module> = emptyList()) {
        if (isInitialized()) return
        val configModule = module {
            includes(modules)
        }
        koinApp = startKoin {
            platformDeclaration()
            modules(configModule)
        }.also {
            it.koin.onLibraryInitialized()
        }

    }

    private fun isInitialized() = koinApp != null


}

private fun Koin.onLibraryInitialized() {
    println("Library is initialized")
}

