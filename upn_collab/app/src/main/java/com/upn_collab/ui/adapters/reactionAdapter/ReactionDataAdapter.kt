package com.upn_collab.ui.adapters.reactionAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.ReactionsResponse

class ReactionDataAdapter(val data:List<ReactionsResponse>,private val clickedIdReaction: (Long) -> Unit)  :
    RecyclerView.Adapter<CustomReactionAdapter>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomReactionAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomReactionAdapter(layoutInflate.inflate(R.layout.card_reactions,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomReactionAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.id_reaction,item.comment,clickedIdReaction)
    }
}