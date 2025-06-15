package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.FavoriteForumDTO
import com.upn_collab.network.dto.response.ForumsAllResponse
import retrofit2.Response
import retrofit2.http.*

interface ForumFavoriteService {
    @GET("api/my-forums-favorites")
    suspend fun getMyForumFavorites():Response<List<ForumsAllResponse>>

    @POST("api/add-favorite")
    suspend fun addFavoriteForum(@Body id_forum: FavoriteForumDTO):Response<Void>

    @DELETE("api/remove-favorite/{foroId}")
    suspend fun removeFavoriteForum(@Path("foroId") foroId: Long):Response<Void>



}