package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.TypeStateDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UpdateStateForumService {

    @PUT("api/update-state/{forumId}")
    suspend fun updateForumState(@Path("forumId") forumId: Long, @Body state: TypeStateDTO): Response<Void>

}