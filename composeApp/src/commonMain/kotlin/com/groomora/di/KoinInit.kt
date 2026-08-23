package com.groomora.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin dependency injection for Compose Multiplatform (Android & iOS).
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            appModule,
            networkModule,
            repositoryModule,
            viewModelModule
        )
    }
