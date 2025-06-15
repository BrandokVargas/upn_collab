package com.upn_collab.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.core.TokenExpiredException
import com.upn_collab.databinding.FragmentForumListAllBinding
import com.upn_collab.network.dto.auth.FavoriteForumDTO

import com.upn_collab.network.dto.response.ForumsAllResponse
import com.upn_collab.network.services.ForumFavoriteService
import com.upn_collab.network.services.ForumsListService
import com.upn_collab.ui.adapters.forumList.ForumDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@AndroidEntryPoint
class ForumListAllFragment : Fragment(),SearchView.OnQueryTextListener {

    @Inject
    lateinit var forumsListService: ForumsListService

    @Inject
    lateinit var forumFavoriteService :ForumFavoriteService

    private var _binding: FragmentForumListAllBinding? = null
    private val binding get() = _binding!!

    private lateinit var forumDataAdapter: ForumDataAdapter
    private val data = mutableListOf<ForumsAllResponse>()

    private var isLastPage = false
    private var isSearching = false
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 5

    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumListAllBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchForum.setOnQueryTextListener(this)
        initRecyclerForums()
        val next : ImageButton = view.findViewById(R.id.addForumIcon)
        next.setOnClickListener{
            val fragment = CreatedForumFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }
        binding.forumList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = layoutManager?.childCount ?: 0
                val totalItemCount = layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0

                if (!isLoading && !isLastPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    getAllForums(currentPage)
                }
            }
        })
        getAllForums(currentPage)
    }

    private fun initRecyclerForums(){
        forumDataAdapter = ForumDataAdapter(data,
            clickedForumDetails = { forumId -> rediretDetailForum(forumId)},
            addFavorite = { forumId,isFavorite -> addFavorite(forumId,isFavorite)}
        )
        binding.forumList.layoutManager = LinearLayoutManager(context)
        binding.forumList.adapter = forumDataAdapter

    }

    private fun addFavorite(forumId: Long,isFavorite:Boolean){
        val forum = data.find { it.idForum == forumId }
        forum?.isFavorite = isFavorite
        forumDataAdapter.notifyDataSetChanged()

        if(isFavorite){
            addFavoriteDB(forumId)
            showToast("Foro añadido a favoritos")

        }else{
            removeFavoriteDB(forumId)
            showToast("Foro eliminado de tu lista de favoritos favoritos")
        }
    }

    private fun addFavoriteDB(forumId: Long) {
        val favoriteForumDTO = FavoriteForumDTO(forumId)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumFavoriteService.addFavoriteForum(favoriteForumDTO)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        getAllForums(currentPage)
                    }
                } else {
                    Log.e("Error", "Error al agregar un favorito ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception", e)
            }
        }
    }

    private fun removeFavoriteDB(forumId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumFavoriteService.removeFavoriteForum(forumId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        getAllForums(currentPage)
                    }
                } else {
                    Log.e("Error", "Error al remover un favorito ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception", e)
            }
        }
    }

    private fun rediretDetailForum(forumId:Long){
        val fragment = EntryForumFragment()

        val args = Bundle()
        args.putLong("FORUM_ID", forumId)
        fragment.arguments = args

        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container,fragment)?.commit()
        println("hola ${forumId}")
    }

    private fun getAllForums(page: Int) {
        if (isSearching) return
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumsListService.getAllForums(page=page,size=pageSize)
                if (response.isSuccessful) {
                    val dataForums = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        forumDataAdapter.addForums(dataForums)
                        currentPage++
                        isLastPage = dataForums.size < pageSize

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar los datos.")
                    }
                }
            } catch (e: TokenExpiredException) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            }finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    isLoading = false
                }
            }
        }
    }

    private fun searchForumByTesisOrArticulo(text:String){
        isSearching = true
        searchText = text
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumsListService.searchtAllForumsByTesisOrArticulo(text)
                if (response.isSuccessful) {
                    val dataForums = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        forumDataAdapter.updateData(dataForums,searchText)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar los datos.")
                    }
                }
            } catch (e: TokenExpiredException) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            }
        }
    }

    override fun onQueryTextSubmit(text: String): Boolean {
       return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        if (!text.isNullOrEmpty()) {
            searchForumByTesisOrArticulo(text)
        } else {
            isSearching = false
            searchText = ""
            forumDataAdapter.clearData() 
            currentPage = 0
            getAllForums(currentPage)
        }
        return true
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}