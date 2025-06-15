package com.upn_collab.network.services

import com.upn_collab.network.dto.response.ForumsAllResponse
import com.upn_collab.network.dto.response.MyForumsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyForumsService {

    @GET("api/my-forums")
    suspend fun getMyForums(@Query("page") page: Int,
                            @Query("size") size: Int):Response<List<MyForumsResponse>>

    @GET("api/search-my-forum/{text}")
    suspend fun searchtMyForumsByTesisOrArticulo(@Path("text") text: String): Response<List<MyForumsResponse>>

}