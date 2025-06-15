package com.upn_collab.ui.adapters.myForumList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.databinding.MyForumListBinding
import org.threeten.bp.format.DateTimeFormatter
import com.upn_collab.R

class CustomMyForumAdapter (view: View): RecyclerView.ViewHolder(view){

    private val binding = MyForumListBinding.bind(view)

    fun bind(typeForum:String,titleForum:String,userName:String,register:String,nameState:String){

        binding.typeForum.text = typeForum
        binding.title.text = titleForum
        binding.userName.text = userName
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = register.format(dateFormatter)
        binding.register.text = formattedDate
        binding.nameState.text = nameState

        when (nameState) {
            "ACEPTADO" -> binding.iconState.setImageResource(R.drawable.aceptado_state)
            "PENDIENTE" -> binding.iconState.setImageResource(R.drawable.pendiente)
            "RECHAZADO" -> binding.iconState.setImageResource(R.drawable.rechazado)
            else -> binding.iconState.setImageResource(R.drawable.pendiente)
        }

    }
}