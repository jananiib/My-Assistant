package com.bot.alvinbot.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bot.alvinbot.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    const val DB_NAME = "MyBotRoom"
    const val PREF_NAME = "MyBotShared"


    @Singleton
    @Provides
    fun getDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

}