package com.rizky.bengkelin.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

    private val USER_TOKEN_KEY = stringPreferencesKey(USER_TOKEN)

    fun getUserToken() = dataStore.data.map {
            it[USER_TOKEN_KEY]
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit {
            it[USER_TOKEN_KEY] = token
        }
    }

    suspend fun deleteUserToken() {
        dataStore.edit {
            it.remove(USER_TOKEN_KEY)
        }
    }

    companion object {
        private const val USER_TOKEN = "user_token"
        const val USER_DATA = "user_data"
    }
}