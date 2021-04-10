package com.github.mik629.aviasales_test_task.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object AppModule {
    @Provides
    @Singleton
    fun provideAppContext(application: Application): Context = application.applicationContext

}
