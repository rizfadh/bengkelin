package com.rizky.bengkelin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationServices
import com.rizky.bengkelin.data.preference.UserPreference
import com.rizky.bengkelin.data.remote.retrofit.ApiConfig
import com.rizky.bengkelin.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = UserPreference.USER_DATA)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context) = UserPreference(context.datastore)

    @Provides
    @Singleton
    fun provideFusedLocation(@ApplicationContext context: Context) = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideUserRepository(preference: UserPreference): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository(preference, apiService)
    }
}