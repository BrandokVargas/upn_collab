package com.upn_collab.network.dto.auth

data class ForumCreatedDTO(val title:String, val url:String,val content:String,
                           val typeForumEntity:TypeForumDTO,val careersRequest:CarrersDTO)