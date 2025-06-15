package com.upn_collab.network.services

import com.upn_collab.network.dto.response.TypeForumResponse
import retrofit2.Response
import retrofit2.http.GET

interface TypeForumService {

    @GET("api/type-forums")
    suspend fun getAllTypeForum(): Response<List<TypeForumResponse>>

}