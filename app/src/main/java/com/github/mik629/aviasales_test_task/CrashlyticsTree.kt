package com.github.mik629.aviasales_test_task

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.ERROR

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(
            "priority: $priority, tag: $tag, message: $message"
        )

        t?.apply { crashlytics.recordException(this) }
    }
}