package com.upn_collab.storage

import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.Date


const val TOKEN_KEY = "jwt"
const val REFRESH_TOKEN_KEY = "jwt_refresh"

class SharePreferencesStorage(val sharedPreferences: SharedPreferences):LocalStorage {

    override fun saveToken(jwt: String) {
        sharedPreferences.edit()
            .putString(TOKEN_KEY,jwt)
            .apply()
    }

    override fun getToken(): String {
        return sharedPreferences.getString(
            TOKEN_KEY,""
        )!!
    }

    override fun isAuthenticated(): Boolean {
        return getToken().isNotEmpty()
    }

    override fun clearToken() {
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .apply()
    }

    override fun getUserRole(): String {
        val token = getToken()
        if (token.isNotEmpty()) {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = parts[1]
                val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
                val decodedString = String(decodedBytes, StandardCharsets.UTF_8)
                val jsonObject = JSONObject(decodedString)
                val authorities = jsonObject.optString("authorities")
                return authorities
            }
        }
        return ""
    }

    override fun isTokenExpired(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size == 3) {
            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, StandardCharsets.UTF_8)
            val jsonObject = JSONObject(decodedString)
            val exp = jsonObject.optLong("exp")
            return exp * 1000 < System.currentTimeMillis()
        }
        return true
    }


}