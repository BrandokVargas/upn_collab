package com.upn_collab.ui.adapters.forumListPending

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.ForumAllPendingResponse
import com.upn_collab.network.dto.response.ForumsAllResponse

class ForumPendingDataAdapter(val data:List<ForumAllPendingResponse>,
                              private val acceptClickListener: (Long,Long) -> Unit,
                              private val refusedClickListener: (Long,Long) -> Unit,
                              private val redirectIntentUrl: (String) -> Unit): RecyclerView.Adapter<CustomForumListPendingAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomForumListPendingAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomForumListPendingAdapter(layoutInflate.inflate(R.layout.card_state_admin,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomForumListPendingAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.idForum,item.type_forum,item.title,item.name,item.register,item.url,item.id_user, acceptClickListener, refusedClickListener,redirectIntentUrl)
    }
}