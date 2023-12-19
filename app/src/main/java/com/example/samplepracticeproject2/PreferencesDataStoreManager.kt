package com.example.samplepracticeproject2

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesDataStoreManager {

    private val EMAIL = stringPreferencesKey("email")
    private val PASSWORD = stringPreferencesKey("password")


    suspend fun writeEmailAndPassword(email: String, password: String) {
        App.application.applicationContext.dataStore.edit { settings ->
            settings[EMAIL] = email
            settings[PASSWORD] = password
            Log.d("SharedViewModel", "Credentials saved: $email, $password")
        }
    }

    fun getEmail(): Flow<String> {
        return App.application.applicationContext.dataStore.data
            .map { preferences ->
                preferences[EMAIL] ?: ""

            }
    }

    fun getPassword(): Flow<String> {
        return App.application.applicationContext.dataStore.data
            .map { preferences ->
                preferences[PASSWORD] ?: ""
            }
    }

    suspend fun clearSession() {
        App.application.applicationContext.dataStore.edit { settings ->
            settings.remove(EMAIL)
            settings.remove(PASSWORD)
        }
    }
}