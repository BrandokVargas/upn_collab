package com.upn_collab.ui.adapters.commentsList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.databinding.CardCommentsBinding
import org.threeten.bp.format.DateTimeFormatter

class CustomCommentAdapter (view: View): RecyclerView.ViewHolder(view) {

    private val binding = CardCommentsBinding.bind(view)
    fun bind(idComment: Long,name:String,comment:String,registerComment:String){
        binding.nameCommentUser.text = name
        binding.contentComment.text = comment
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = registerComment.format(dateFormatter)
        binding.fechaComment.text = formattedDate

    }



}