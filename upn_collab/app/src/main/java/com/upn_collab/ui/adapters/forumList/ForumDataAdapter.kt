package com.upn_collab.ui.adapters.forumList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.ForumsAllResponse

class ForumDataAdapter(private val data:MutableList<ForumsAllResponse>,
                       private val clickedForumDetails: (Long) -> Unit,
                       private val addFavorite: (Long,Boolean) -> Unit):RecyclerView.Adapter<CustomForumAdapter>() {

    private var searchText: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomForumAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        //return CustomForumAdapter(layoutInflate.inflate(R.layout.all_forum_list,parent,false))
        val view = layoutInflate.inflate(R.layout.all_forum_list, parent, false)
        return CustomForumAdapter(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomForumAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.idForum,item.type_forum,item.title,item.name,item.register,item.hour,item.isFavorite,clickedForumDetails,addFavorite,searchText)
    }

    fun updateData(newData: List<ForumsAllResponse>, searchText: String="") {
        this.searchText = searchText
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun addForums(newForums: List<ForumsAllResponse>) {
        val startPosition = data.size
        data.addAll(newForums)
        notifyItemRangeInserted(startPosition, newForums.size)
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

}