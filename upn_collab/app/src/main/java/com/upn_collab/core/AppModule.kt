package com.upn_collab.core

import android.content.Context
import com.upn_collab.storage.LocalStorage
import com.upn_collab.storage.SharePreferencesStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext


const val SHARED_PREFERENCES_NAME =  "shared_collab"

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideLocalStorage(@ApplicationContext appContext:Context): LocalStorage{
        val sharedPreference = appContext.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return SharePreferencesStorage(sharedPreference)
    }

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

}