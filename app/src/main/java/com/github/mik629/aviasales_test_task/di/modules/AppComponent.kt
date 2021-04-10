package com.github.mik629.aviasales_test_task.di.modules

import android.app.Application
import com.github.mik629.aviasales_test_task.App
import com.github.mik629.aviasales_test_task.presentation.ui.AppActivity
import com.github.mik629.aviasales_test_task.presentation.ui.destinations.ChooseDestinationsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {
    //    fun provideRouter(): Router

    fun inject(app: App)

    fun inject(activity: AppActivity)

    fun inject(fragment: ChooseDestinationsFragment)

    @Component.Factory
    interface Factory {
        fun create(
                @BindsInstance
                application: Application
        ): AppComponent
    }
}
