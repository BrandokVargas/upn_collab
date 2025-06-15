package com.upn_collab.ui.adapters.forumList


import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.databinding.AllForumListBinding
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter



class CustomForumAdapter (view:View):RecyclerView.ViewHolder(view){

    private val binding = AllForumListBinding.bind(view)
    private var isFavorite: Boolean = false

    fun bind(forumId: Long,typeForum:String,titleForum:String,userName:String,register:String,hour:String,isFavorite:Boolean,
             clickedForumDetails: (Long) -> Unit,addFavorite: (Long,Boolean) -> Unit,searchText: String){

        binding.typeForum.text = getHighlightedText(typeForum,searchText)
        binding.title.text = getHighlightedText(titleForum, searchText)
        binding.userName.text = getHighlightedText(userName,searchText)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = register.format(dateFormatter)
        binding.register.text = getHighlightedText(formattedDate,searchText)


        binding.hour.text = getHighlightedText(hour,searchText)

        binding.forumEntryAll.setOnClickListener{
            clickedForumDetails(forumId)
        }

        this.isFavorite = isFavorite
        updateFavoriteIcon(isFavorite)

        binding.selectedFavorite.setOnClickListener{
            this.isFavorite = !this.isFavorite
            updateFavoriteIcon(this.isFavorite)
            addFavorite(forumId,this.isFavorite)
        }

    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.selectedFavorite.setImageResource(R.drawable.select_favorite)
        } else {
            binding.selectedFavorite.setImageResource(R.drawable.not_favorite)
        }
    }
    private fun getHighlightedText(text: String, searchText: String): SpannableString {
        val spannableString = SpannableString(text)
        if (searchText.isNotEmpty()) {
            val startIndex = text.toLowerCase().indexOf(searchText.toLowerCase())
            if (startIndex >= 0) {
                val endIndex = startIndex + searchText.length
                spannableString.setSpan(BackgroundColorSpan(Color.YELLOW), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableString
    }
}