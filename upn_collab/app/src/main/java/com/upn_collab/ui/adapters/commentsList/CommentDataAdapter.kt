package com.upn_collab.ui.adapters.commentsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.CommentsResponse

class CommentDataAdapter(val data: List<CommentsResponse>) :
    RecyclerView.Adapter<CustomCommentAdapter>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCommentAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomCommentAdapter(layoutInflate.inflate(R.layout.card_comments,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomCommentAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.idComment,item.name,item.comment,item.registerComment)
    }

}