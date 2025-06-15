package com.upn_collab.core


import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.upn_collab.network.AuthInterceptor
import com.upn_collab.network.services.*
import com.upn_collab.storage.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ActivityComponent::class,FragmentComponent::class)
object NetworkServiceModule {


    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthServices{
        return retrofit.create(AuthServices::class.java)
    }

    @Provides
    fun provideForumsListService(retrofit: Retrofit): ForumsListService{
        return retrofit.create(ForumsListService::class.java)
    }

    @Provides
    fun provideTypeForumList(retrofit: Retrofit): TypeForumService{
        return retrofit.create(TypeForumService::class.java)
    }

    @Provides
    fun provideCreatedForum(retrofit: Retrofit): CreatedForumService{
        return retrofit.create(CreatedForumService::class.java)
    }

    @Provides
    fun provideGetMyForums(retrofit: Retrofit): MyForumsService{
        return retrofit.create(MyForumsService::class.java)
    }
    @Provides
    fun provideGetAllListForumsPending(retrofit: Retrofit): ForumListPendingService{
        return retrofit.create(ForumListPendingService::class.java)
    }

    @Provides
    fun provideUpdateStateForumService(retrofit: Retrofit): UpdateStateForumService{
        return retrofit.create(UpdateStateForumService::class.java)
    }

    @Provides
    fun provideGetCommentForForums(retrofit: Retrofit):CommentService {
        return retrofit.create(CommentService::class.java)
    }

    @Provides
    fun provideGetFavoriteForums(retrofit: Retrofit):ForumFavoriteService{
        return retrofit.create(ForumFavoriteService::class.java)
    }

    @Provides
    fun provideRegisterDevice(retrofit: Retrofit):DeviceService{
        return retrofit.create(DeviceService::class.java)
    }

    @Provides
    fun provideUserService(retrofit: Retrofit):UserService{
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun providesRetrofit(localStorage: LocalStorage,@ApplicationContext context: Context): Retrofit{

        val loginInterceptor= okhttp3.logging.HttpLoggingInterceptor()
        loginInterceptor.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        val okHttpClient = okhttp3.OkHttpClient.Builder().addInterceptor(loginInterceptor)
            .writeTimeout(0,java.util.concurrent.TimeUnit.MILLISECONDS)
            .readTimeout(2,java.util.concurrent.TimeUnit.MINUTES)
            .connectTimeout(1,java.util.concurrent.TimeUnit.MINUTES)
            .addInterceptor(AuthInterceptor(localStorage,context))
            .build()

        val gson = com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create()

        return Retrofit.Builder().baseUrl("http://192.168.101.6:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}