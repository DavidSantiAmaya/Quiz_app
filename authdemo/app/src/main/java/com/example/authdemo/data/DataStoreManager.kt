
package com.example.authdemo.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class DataStoreManager(private val context: Context) {
    private val TOKEN = stringPreferencesKey("token")
    val tokenFlow = context.dataStore.data.map { prefs -> prefs[TOKEN] ?: "" }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs -> prefs[TOKEN] = token }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs -> prefs.remove(TOKEN) }
    }
}
