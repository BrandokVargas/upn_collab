package com.upn_collab.ui.adapters.entryForum

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.databinding.CardEntryCommentBinding
import org.threeten.bp.format.DateTimeFormatter

class CustomEntryCommentAdapter(view: View): RecyclerView.ViewHolder(view) {

    private val binding = CardEntryCommentBinding.bind(view)

    fun bind(id_forum:Long,title: String,name_user:String,content:String,
             url:String ,date_register:String,clickedUrlForum: (String) -> Unit){
        binding.title.text = title
        binding.content.text = content
        binding.userName.text = name_user
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = date_register.format(dateFormatter)
        binding.register.text = formattedDate

        binding.share.setOnClickListener{
            clickedUrlForum(url)
        }

    }



}