package com.bot.alvinbot.di

import android.app.Application
import android.content.Context
import com.bot.alvinbot.utils.constants.FirebaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideUserCollectionFirebaseFireStore(): CollectionReference {
        return Firebase.firestore.collection(FirebaseConstants.USER)
    }

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext
}