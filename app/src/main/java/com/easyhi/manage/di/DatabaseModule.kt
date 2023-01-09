package com.easyhi.manage.di

import android.content.Context
import android.content.SharedPreferences
import com.easyhi.manage.data.AppDatabase
import com.easyhi.manage.util.SpUtil
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
    fun providePreferences(): SharedPreferences {
        return SpUtil.getSp()
    }


}