package com.example.authdemo.data

import android.util.Log
import com.example.authdemo.network.AuthApi
import com.example.authdemo.network.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val api: AuthApi, private val ds: DataStoreManager) {
    var lastError = "" // ← AÑADE ESTO PARA DEBUG

    suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d("AuthRepo", "🔐 Usuario: '$username'")
            Log.d("AuthRepo", "🔐 Password: '$password'")

            val resp = api.login(LoginRequest(username, password))

            Log.d("AuthRepo", "📡 Código: ${resp.code()}")
            Log.d("AuthRepo", "📡 Exitoso: ${resp.isSuccessful}")

            lastError = "Código HTTP: ${resp.code()}, Exitoso: ${resp.isSuccessful}" // ← DEBUG

            if (resp.isSuccessful) {
                val body = resp.body()
                Log.d("AuthRepo", "📦 Body: $body")
                Log.d("AuthRepo", "📦 Token: ${body?.token}")

                lastError += ", Body: $body, Token: ${body?.token}" // ← DEBUG

                if (body != null && body.token.isNotBlank()) {
                    ds.saveToken(body.token)
                    Log.d("AuthRepo", "✅ TOKEN GUARDADO!")
                    lastError = "✅ Login exitoso"
                    true
                } else {
                    Log.e("AuthRepo", "❌ BODY NULL O TOKEN VACÍO")
                    lastError = "❌ Body null o token vacío"
                    false
                }
            } else {
                val errorBody = resp.errorBody()?.string()
                Log.e("AuthRepo", "❌ ERROR HTTP: ${resp.code()}")
                Log.e("AuthRepo", "❌ Mensaje: $errorBody")
                lastError = "❌ HTTP ${resp.code()}: $errorBody"
                false
            }
        } catch (e: Exception) {
            Log.e("AuthRepo", "❌ EXCEPCIÓN: ${e.message}")
            e.printStackTrace()
            lastError = "❌ Excepción: ${e.message}"
            false
        }
    }

    fun tokenFlow() = ds.tokenFlow
    suspend fun logout() = ds.clearToken()
}