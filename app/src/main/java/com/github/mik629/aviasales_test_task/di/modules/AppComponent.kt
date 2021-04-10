package com.github.mik629.aviasales_test_task.di.modules

import android.app.Application
import com.github.mik629.aviasales_test_task.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {
    //    fun provideRouter(): Router

    fun inject(app: App)

//    fun inject(fragment: FragmentMoviesList)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: Application
        ): AppComponent
    }
}
