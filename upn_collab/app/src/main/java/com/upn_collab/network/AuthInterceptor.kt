package com.upn_collab.network

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.upn_collab.core.TokenExpiredException
import com.upn_collab.storage.LocalStorage
import com.upn_collab.ui.view.MainActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val localStorage: LocalStorage,private val context: Context):Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
        val jwt = localStorage.getToken()
        if(jwt.isNotEmpty()){
            if(localStorage.isTokenExpired(jwt)){
                closeSession()
                localStorage.clearToken()
                throw TokenExpiredException("Token expired")
            }
            req.addHeader("Authorization","Bearer ${jwt}")
        }

        return chain.proceed(req.build())
    }

    private fun closeSession() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Token expirado. Redirigiendo al login...", Toast.LENGTH_LONG).show()
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}