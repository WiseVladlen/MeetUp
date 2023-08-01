package com.example.meet_up.di

import android.content.Context
import com.example.meet_up.data.local.DataBase
import com.example.meet_up.data.remote.HttpClientFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {
    @Singleton
    @Provides
    fun provideDataBase(context: Context): DataBase {
        return DataBase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun getHttpClientFactory(): HttpClientFactory {
        return HttpClientFactory()
    }
}