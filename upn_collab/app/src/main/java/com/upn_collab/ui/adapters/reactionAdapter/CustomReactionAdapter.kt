package com.upn_collab.ui.adapters.reactionAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.databinding.CardReactionsBinding

class CustomReactionAdapter (view: View): RecyclerView.ViewHolder(view) {

    private val binding = CardReactionsBinding.bind(view)

    fun bind(id_reaction: Long,comment:String,
             clickedIdReaction: (Long) -> Unit){
        binding.textReaction.text = comment

        binding.textReaction.setOnClickListener{
            clickedIdReaction(id_reaction)
        }

    }


}