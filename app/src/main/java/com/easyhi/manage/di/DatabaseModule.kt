package com.easyhi.manage.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.easyhi.manage.data.local.AppDatabase
import com.easyhi.manage.data.local.tokenDataStore
import com.easyhi.manage.serialize.AuthTokenP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideAuthTokenStore(@ApplicationContext context: Context): DataStore<AuthTokenP> {
        return context.tokenDataStore
    }

}