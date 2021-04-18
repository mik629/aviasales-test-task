package com.github.mik629.aviasales_test_task

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.github.mik629.aviasales_test_task.di.modules.AppComponent
import com.github.mik629.aviasales_test_task.di.modules.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        Timber.plant(CrashlyticsTree())
    }
}

val Activity.appComponent get(): AppComponent = (application as App).appComponent
val Fragment.appComponent get(): AppComponent = requireActivity().appComponent
