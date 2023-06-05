package com.rizky.bengkelin.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rizky.bengkelin.model.UserData
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {
    private val USER_TOKEN_KEY = stringPreferencesKey(USER_TOKEN)
    private val USER_NAME_KEY = stringPreferencesKey(USER_NAME)
    private val USER_EMAIL_KEY = stringPreferencesKey(USER_EMAIL)

    fun getUserData() = dataStore.data.map {
        UserData(
            it[USER_TOKEN_KEY],
            it[USER_NAME_KEY],
            it[USER_EMAIL_KEY]
        )
    }

    suspend fun saveUserData(token: String, name: String, email: String) {
        dataStore.edit {
            it[USER_TOKEN_KEY] = token
            it[USER_NAME_KEY] = name
            it[USER_EMAIL_KEY] = email
        }
    }

    suspend fun deleteUserData() {
        dataStore.edit {
            it.remove(USER_TOKEN_KEY)
            it.remove(USER_NAME_KEY)
            it.remove(USER_EMAIL_KEY)
        }
    }

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        const val USER_DATA = "user_data"
    }
}