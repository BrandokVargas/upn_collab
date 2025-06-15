package com.upn_collab.ui.adapters.myForumList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.network.dto.response.MyForumsResponse


class MyForumDataAdapter(val data:List<MyForumsResponse>) : RecyclerView.Adapter<CustomMyForumAdapter>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomMyForumAdapter {
        val layoutInflate = LayoutInflater.from(parent.context)
        return CustomMyForumAdapter(layoutInflate.inflate(R.layout.my_forum_list,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomMyForumAdapter, position: Int) {
        val item = data[position]
        holder.bind(item.type_forum,item.title,item.name,item.register,item.name_state)
    }
}