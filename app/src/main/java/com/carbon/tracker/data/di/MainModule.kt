package com.carbon.tracker.data.di

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.carbon.tracker.data.local.preferences.MainLocal
import com.carbon.tracker.data.local.preferences.MainLocalImpl
import com.carbon.tracker.data.local.preferences.PreferenceHelperImpl
import com.carbon.tracker.data.repository.MainRepositoryImpl
import com.carbon.tracker.ui.repository.MainRepository
import com.carbon.tracker.ui.repository.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "com.carbon.tracker",
            masterKeyAlias,
            app,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun providePreference(sharedPreferences: SharedPreferences): PreferenceHelper {
        return PreferenceHelperImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(
    ): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseReference(
    ): DatabaseReference {
        return Firebase.database.reference
    }

    /* Locals */
    @Provides
    @Singleton
    fun provideMainLocal(
        preferenceHelper: PreferenceHelper
    ): MainLocal {
        return MainLocalImpl(
            preferenceHelper
        )
    }

    /* Repositories */
    @Provides
    @Singleton
    fun provideMainRepository(
        firebaseAuth: FirebaseAuth,
        databaseReference: DatabaseReference,
        mainLocal: MainLocal,
    ): MainRepository {
        return MainRepositoryImpl(
            firebaseAuth, databaseReference, mainLocal
        )
    }
}