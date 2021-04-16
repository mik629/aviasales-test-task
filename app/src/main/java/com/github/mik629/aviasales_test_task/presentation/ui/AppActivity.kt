package com.github.mik629.aviasales_test_task.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mik629.aviasales_test_task.R
import com.github.mik629.aviasales_test_task.Screens
import com.github.mik629.aviasales_test_task.appComponent
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class AppActivity : AppCompatActivity() {
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val navigator = AppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContentView(R.layout.app_activity)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.destinationsFragment())
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}