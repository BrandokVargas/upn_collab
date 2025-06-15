package com.upn_collab.ui.adapters.forumListPending

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.upn_collab.databinding.CardStateAdminBinding

import org.threeten.bp.format.DateTimeFormatter



class CustomForumListPendingAdapter(view: View): RecyclerView.ViewHolder(view) {

    private val binding = CardStateAdminBinding.bind(view)

    fun bind(forumId: Long,typeForum:String,titleForum:String,userName:String,register:String,url:String,id_user:Long,
             acceptClickListener: (Long,Long) -> Unit,
             refusedClickListener: (Long,Long) -> Unit,
             redirectIntentUrl: (String) -> Unit
    ){

        binding.typeForum.text = typeForum
        binding.title.text = titleForum
        binding.userName.text = userName
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = register.format(dateFormatter)


        binding.register.text = formattedDate

        binding.iconAccept.setOnClickListener {
            acceptClickListener(forumId,id_user)
        }

        binding.iconRefused.setOnClickListener {
            refusedClickListener(forumId,id_user)
        }
        binding.url.setOnClickListener{
            redirectIntentUrl(url)
        }


        if (url != null) {
            binding.url.setOnClickListener {
                redirectIntentUrl(url)
            }
        } else {
            binding.url.setOnClickListener(null)
        }

    }


}