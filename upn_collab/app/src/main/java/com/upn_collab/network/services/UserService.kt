package com.upn_collab.network.services


import com.upn_collab.network.dto.response.ProfileUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService{

    @GET("api/profile")
    suspend fun getProfileUserAuthenticate(): Response<ProfileUserResponse>

    @PUT("api/update-nivel")
    suspend fun addNivelUpdateUser(): Response<Void>



}