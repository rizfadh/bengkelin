package com.rizky.bengkelin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rizky.bengkelin.data.preference.UserPreference
import com.rizky.bengkelin.data.remote.retrofit.BengkelinApiConfig
import com.rizky.bengkelin.data.remote.retrofit.OsrmApiConfig
import com.rizky.bengkelin.data.remote.retrofit.PredictionApiConfig
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
    fun provideUserPreference(
        @ApplicationContext context: Context
    ) = UserPreference(context.datastore)

    @Provides
    @Singleton
    fun provideUserRepository(
        preference: UserPreference
    ): UserRepository {
        val bengkelinApiService = BengkelinApiConfig.getApiService()
        val predictionApiService = PredictionApiConfig.getApiService()
        val osrmApiService = OsrmApiConfig.getApiService()
        return UserRepository(
            preference,
            bengkelinApiService,
            predictionApiService,
            osrmApiService
        )
    }
}