package com.upn_collab.ui.adapters.favoriteListForums

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.databinding.CardForumsFavoritesBinding
import org.threeten.bp.format.DateTimeFormatter

class CustomFavoriteForumAdapter (view: View): RecyclerView.ViewHolder(view){

    private val binding = CardForumsFavoritesBinding.bind(view)

    fun bind(forumId: Long,typeForum:String,titleForum:String,userName:String,register:String,
             clickedForumFavorite: (Long) -> Unit){

        binding.typeForum.text = typeForum
        binding.title.text = titleForum
        binding.userName.text = userName
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = register.format(dateFormatter)
        binding.register.text = formattedDate

        binding.removeFavorite.setOnClickListener{
            clickedForumFavorite(forumId)
        }

    }


}