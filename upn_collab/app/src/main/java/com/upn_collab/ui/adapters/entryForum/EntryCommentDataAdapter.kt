package com.upn_collab.ui.adapters.entryForum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.EntryForumResponse


class EntryCommentDataAdapter(val data: List<EntryForumResponse>,private val clickedUrlForum: (String) -> Unit) :
    RecyclerView.Adapter<CustomEntryCommentAdapter>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomEntryCommentAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomEntryCommentAdapter(layoutInflate.inflate(R.layout.card_entry_comment,parent,false))
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: CustomEntryCommentAdapter, position: Int) {
        val item = data[0]
        holder.bind(item.id_forum,item.title,item.name_user,item.content,item.url,item.date_register,clickedUrlForum)
    }

}