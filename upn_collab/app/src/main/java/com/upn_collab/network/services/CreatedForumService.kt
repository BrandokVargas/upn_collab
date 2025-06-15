package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.ForumCreatedDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreatedForumService {
    @POST("api/add-forum")
    suspend fun createdForum(@Body forumCreatedDTO: ForumCreatedDTO) : Response<Void>
}