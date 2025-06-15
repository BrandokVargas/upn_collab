package com.upn_collab.storage


interface LocalStorage {
    fun saveToken(token: String)
    fun getToken(): String
    fun isAuthenticated():Boolean
    fun clearToken()
    fun getUserRole(): String
    fun isTokenExpired(token: String): Boolean
}