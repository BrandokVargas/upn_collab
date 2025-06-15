package com.upn_collab.ui.adapters.favoriteListForums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.ForumsAllResponse

class FavoriteForumDataAdapter(val data:List<ForumsAllResponse>,
                               private val clickedForumFavorite: (Long) -> Unit):
    RecyclerView.Adapter<CustomFavoriteForumAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomFavoriteForumAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomFavoriteForumAdapter(layoutInflate.inflate(R.layout.card_forums_favorites,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomFavoriteForumAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.idForum,item.type_forum,item.title,item.name,item.register,clickedForumFavorite)
    }


}