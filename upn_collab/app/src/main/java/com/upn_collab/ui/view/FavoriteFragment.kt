package com.upn_collab.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.upn_collab.R
import com.upn_collab.databinding.FragmentFavoriteBinding
import com.upn_collab.network.dto.response.ForumsAllResponse
import com.upn_collab.network.dto.response.MyForumsResponse
import com.upn_collab.network.services.ForumFavoriteService
import com.upn_collab.ui.adapters.favoriteListForums.FavoriteForumDataAdapter
import com.upn_collab.ui.adapters.myForumList.MyForumDataAdapter

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    @Inject
    lateinit var forumFavoriteService: ForumFavoriteService
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteForumDataAdapter: FavoriteForumDataAdapter
    private val data = mutableListOf<ForumsAllResponse>()
    private val filteredData = mutableListOf<ForumsAllResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerMyForumsFavorites()
        val next : ImageButton = view.findViewById(R.id.addForum)
        next.setOnClickListener{
            val fragment = CreatedForumFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }
        getMyForumsListFavorites()

    }

    private fun initRecyclerMyForumsFavorites(){
        favoriteForumDataAdapter = FavoriteForumDataAdapter(data) { idForum ->
            removeFavorite(
                idForum
            )
        }
        binding.myForumsListFavorites.layoutManager = LinearLayoutManager(context)
        binding.myForumsListFavorites.adapter = favoriteForumDataAdapter
    }

    private fun removeFavorite(forumId: Long){
        removeFavoriteDB(forumId)
        showToast("Foro eliminado de tu lista de favoritos favoritos")
    }


    private fun removeFavoriteDB(forumId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumFavoriteService.removeFavoriteForum(forumId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        getMyForumsListFavorites()
                    }
                } else {
                    Log.e("Error", "Error al remover un favorito ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception", e)
            }
        }

    }

    private fun getMyForumsListFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumFavoriteService.getMyForumFavorites()
                if (response.isSuccessful) {
                    val dataForums = response.body()
                    withContext(Dispatchers.Main) {
                        dataForums?.let {
                            data.clear()
                            data.addAll(it)
                            favoriteForumDataAdapter.notifyDataSetChanged()
                            if (it.isEmpty()) {
                                binding.myForumsListFavorites.visibility = View.GONE
                                binding.emptyView.visibility = View.VISIBLE
                            } else {
                                binding.myForumsListFavorites.visibility = View.VISIBLE
                                binding.emptyView.visibility = View.GONE
                            }

                        }
                    }
                } else {
                    Log.e("ForumListAllFragment", "Error getting my forums: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ForumListAllFragment", "Exception getting my forums", e)
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}