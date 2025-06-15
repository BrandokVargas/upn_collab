package com.upn_collab.network.services

import com.upn_collab.network.dto.response.ForumsAllResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ForumsListService {
    @GET("api/all-forum")
    suspend fun getAllForums(@Query("state") state: Long = 2,@Query("page") page: Int,
                             @Query("size") size: Int): Response<List<ForumsAllResponse>>

    @GET("api/search-forum/{text}")
    suspend fun searchtAllForumsByTesisOrArticulo(@Path("text") text: String): Response<List<ForumsAllResponse>>

}