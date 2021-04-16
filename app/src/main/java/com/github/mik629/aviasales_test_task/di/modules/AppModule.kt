package com.github.mik629.aviasales_test_task.di.modules

import android.app.Application
import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object AppModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideRouter(): Router =
            cicerone.router

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder =
            cicerone.getNavigatorHolder()

    @Provides
    @Singleton
    fun provideAppContext(application: Application): Context =
        application.applicationContext
}
