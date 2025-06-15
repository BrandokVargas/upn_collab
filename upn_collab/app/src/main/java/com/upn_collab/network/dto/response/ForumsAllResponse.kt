package com.upn_collab.network.dto.response

data class ForumsAllResponse(val idForum:Long, val type_forum: String, val title:String,
                             val name:String,
                             val register:String,
                             val hour:String,
                             var isFavorite:Boolean, val id_user:Long)
