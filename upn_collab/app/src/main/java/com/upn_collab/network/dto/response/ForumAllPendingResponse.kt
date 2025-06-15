package com.upn_collab.network.dto.response

data class ForumAllPendingResponse(val idForum:Long,val type_forum: String,val title:String,
                                   val name:String,
                                   val register:String,val url:String,val id_user:Long)
