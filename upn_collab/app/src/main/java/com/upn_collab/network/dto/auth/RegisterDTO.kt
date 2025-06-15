package com.upn_collab.network.dto.auth

data class RegisterDTO(val cod_upn: String,val name:String,val lastname:String,
                       val email:String,val careersRequest: CarrersDTO)


