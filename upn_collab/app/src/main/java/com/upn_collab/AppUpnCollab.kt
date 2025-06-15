package com.upn_collab

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.upn_collab.core.SHARED_PREFERENCES_NAME
import com.upn_collab.storage.LocalStorage
import com.upn_collab.storage.SharePreferencesStorage
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class AppUpnCollab:Application(){



    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}