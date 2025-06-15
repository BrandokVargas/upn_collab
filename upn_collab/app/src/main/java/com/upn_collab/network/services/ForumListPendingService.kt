package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.TypeStateDTO
import com.upn_collab.network.dto.response.ForumAllPendingResponse
import com.upn_collab.network.dto.response.ForumsAllResponse
import retrofit2.Response
import retrofit2.http.GET

interface ForumListPendingService {
    @GET("api/all-forum-pending")
    suspend fun getAllForumsPending(): Response<List<ForumAllPendingResponse>>
}