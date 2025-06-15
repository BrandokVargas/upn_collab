package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.ReactionCommentDTO
import com.upn_collab.network.dto.response.CommentsResponse
import com.upn_collab.network.dto.response.EntryForumResponse
import com.upn_collab.network.dto.response.ReactionsResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @GET("api/comments/{id_forum}")
    suspend fun getCommentForId(@Path("id_forum") id_forum: Long?,@Query("page") page: Int,
                                @Query("size") size: Int): Response<List<CommentsResponse>>

    @GET("api/entry-forum-comment/{id_forum}")
    suspend fun getEntryForumComment(@Path("id_forum") id_forum: Long?): Response<List<EntryForumResponse>>

    @GET("api/all-reactions")
    suspend fun getAllReactionsComments():Response<List<ReactionsResponse>>

    @POST("api/add-comment")
    suspend fun addComment(@Body reactionCommentDTO: ReactionCommentDTO):Response<Void>


}