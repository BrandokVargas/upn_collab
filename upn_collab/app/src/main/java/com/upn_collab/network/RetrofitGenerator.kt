package com.upn_collab.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitGenerator {

    companion object{
        fun getInstance(): Retrofit{

            val loginInteruptor = HttpLoggingInterceptor()
            loginInteruptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loginInteruptor)
                .writeTimeout(0,TimeUnit.MILLISECONDS)
                .readTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(1,TimeUnit.MINUTES).build()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .create()

            return Retrofit.Builder()
                .baseUrl("http://192.168.101.6:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        }
    }
}