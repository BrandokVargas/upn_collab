package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.LoginDTO
import com.upn_collab.network.dto.auth.RegisterDTO
import com.upn_collab.network.dto.auth.TokenDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {

    @POST("auth/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<TokenDTO>


    @POST("auth/register")
    suspend fun register(@Body registerDTO: RegisterDTO): Response<TokenDTO>

}